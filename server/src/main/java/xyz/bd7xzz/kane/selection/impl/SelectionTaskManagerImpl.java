package xyz.bd7xzz.kane.selection.impl;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.constraint.SelectionConstraints;
import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.po.SelectionTaskHistoryPO;
import xyz.bd7xzz.kane.po.SelectionTaskPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.selection.SelectionConfigManager;
import xyz.bd7xzz.kane.selection.SelectionProcessHandler;
import xyz.bd7xzz.kane.selection.SelectionSignalRegister;
import xyz.bd7xzz.kane.selection.SelectionTaskManager;
import xyz.bd7xzz.kane.selection.repository.SelectionTaskHistoryRepository;
import xyz.bd7xzz.kane.selection.repository.SelectionTaskRepository;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.util.TimerUtil;
import xyz.bd7xzz.kane.vo.SelectionConfigVO;
import xyz.bd7xzz.kane.vo.SelectionTaskVO;
import xyz.bd7xzz.kane.vo.TaskContextVO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选任务管理器
 * @date 7/25/21 10:52 PM
 */
@Service
@Slf4j
public class SelectionTaskManagerImpl implements SelectionTaskManager {

    private final SelectionConfigManager selectionConfigManager;
    private final SnowFlakeProperties snowFlakeProperties;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final LocalCache localCache;
    private final SelectionTaskRepository selectionTaskRepository;
    private final SelectionTaskHistoryRepository selectionTaskHistoryRepository;

    public SelectionTaskManagerImpl(SelectionConfigManager selectionConfigManager, SnowFlakeProperties snowFlakeProperties,
                                    @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor, LocalCache localCache,
                                    SelectionTaskRepository selectionTaskRepository, SelectionTaskHistoryRepository selectionTaskHistoryRepository) {
        this.selectionConfigManager = selectionConfigManager;
        this.snowFlakeProperties = snowFlakeProperties;
        this.taskExecutor = taskExecutor;
        this.localCache = localCache;
        this.selectionTaskRepository = selectionTaskRepository;
        this.selectionTaskHistoryRepository = selectionTaskHistoryRepository;
    }

    @Override
    public List<Long> findTaskIdsBySelectionConfigId(long id) {
        return selectionTaskRepository.getTaskIdByConfigId(id);
    }

    @Override
    public void stopTask(long id) {
        SelectionTaskVO selectionTaskVO = getSelectionTaskVO(id);
        optionTaskStatus(selectionTaskVO, SelectionTaskSignalConstraint.STOPPED,
                SelectionConstraints.TASK_STATE_DONE, SelectionConstraints.TASK_MESSAGE_CODE_MANUAL_STOP);
    }

    @Override
    public void pauseTask(long taskId) {
        SelectionTaskVO selectionTaskVO = getSelectionTaskVO(taskId);
        optionTaskStatus(selectionTaskVO, SelectionTaskSignalConstraint.PAUSE,
                SelectionConstraints.TASK_STATE_PAUSED, SelectionConstraints.TASK_MESSAGE_CODE_MANUAL_PAUSE);
    }

    @Override
    public void resumeTask(long taskId) {
        SelectionTaskVO selectionTaskVO = getSelectionTaskVO(taskId);
        optionTaskStatus(selectionTaskVO, SelectionTaskSignalConstraint.RESUME,
                SelectionConstraints.TASK_STATE_RUNNING, SelectionConstraints.TASK_MESSAGE_CODE_SUCCESS);
    }

    @Override
    public long executeSelection(long configId) {
        SelectionConfigVO selectionConfigVO = selectionConfigManager.getSelectionFromCache(configId);
        try {
            selectionConfigVO = BeanUtil.copy(selectionConfigVO, SelectionConfigVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("executeSelection copy selectionConfigVO error!", e);
            throw new KaneRuntimeException("execute error");
        }
        long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
        String threadName = buildThreadName(id);
        SelectionTaskVO task = SelectionTaskVO.builder()
                .taskId(id)
                .configId(configId)
                .executeTime(DateFormatUtils.format(new Date(), SelectionConstraints.DATE_FORMAT))
                .build();
        TaskContextVO context = TaskContextVO.builder()
                .task(task)
                .selectionConfig(selectionConfigVO)
                .currentThreadName(threadName)
                .build();
        if (StringUtils.isNotEmpty(selectionConfigVO.getCron())) {
            Calendar calendar = cronToCalendar(selectionConfigVO.getCron());
            task.setStatus(SelectionConstraints.TASK_STATE_WAITING);
            submitScheduledTask(context, calendar);
        } else {
            task.setStatus(SelectionConstraints.TASK_STATE_RUNNING);
            taskExecutor.submitListenable(() -> {
                Thread.currentThread().setName(context.getCurrentThreadName());
                SelectionProcessHandler.doProcess(context); //执行筛选
            }).addCallback(new ListenableFutureCallback<Object>() {
                @Override
                public void onSuccess(Object o) {
                    log.info("{} submitScheduledTask-SelectionProcessHandler.doProcess success!", context.getCurrentThreadName());
                    SelectionSignalRegister.clear(context.getTask().getTaskId());
                    setStatusToCache(SelectionConstraints.TASK_STATE_DONE, task);
                    selectionTaskHistoryRepository.updateStatus(id, SelectionConstraints.TASK_STATE_DONE, context.getSelectionResult().toString(), SelectionConstraints.TASK_MESSAGE_CODE_SUCCESS);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    log.error(context.getCurrentThreadName() + " taskExecutor-SelectionProcessHandler.doProcess error!", throwable);
                    SelectionSignalRegister.clear(context.getTask().getTaskId());
                    setStatusToCache(SelectionConstraints.TASK_STATE_ERROR, task);
                    selectionTaskHistoryRepository.updateStatus(id, SelectionConstraints.TASK_STATE_ERROR, throwable.getMessage(),
                            throwable instanceof KaneRuntimeException ? SelectionConstraints.TASK_MESSAGE_CODE_BIZ_ERROR : SelectionConstraints.TASK_MESSAGE_CODE_BIZ_JAVA_ERROR);
                }
            });
        }
        localCache.getSelectionTaskCache().put(id, task);
        SelectionTaskPO taskPO = buildTaskPO(task, selectionConfigVO);
        selectionTaskRepository.save(taskPO);
        SelectionTaskHistoryPO taskHistoryPO = buildTaskHistoryPO(id, task.getStatus());
        selectionTaskHistoryRepository.save(taskHistoryPO);
        return id;
    }

    @Override
    public SelectionTaskVO getTask(long taskId) {
        SelectionTaskVO taskVO = localCache.getSelectionTaskCache().getIfPresent(taskId);
        if (null == taskVO) {
            taskVO = loadTask(taskId);
        }
        return taskVO;
    }

    @Override
    public void single(long taskId, SelectionTaskSignalConstraint signalConstraint) {
        TaskContextVO taskContext = localCache.getTaskContextCache().get(taskId);
        if (null == taskContext) {
            log.warn("ignore single {} with task id {},task not found", signalConstraint, taskId);
            return;
        }

        while (true) {
            if (taskContext.setSignalFlag(signalConstraint)) {
                return;
            }
            Uninterruptibles.sleepUninterruptibly(20, TimeUnit.MICROSECONDS);
        }
    }

    /**
     * 构建筛选任务历史
     *
     * @param taskId 任务id
     * @param status 状态
     * @return 筛选任务历史
     */
    private SelectionTaskHistoryPO buildTaskHistoryPO(long taskId, short status) {
        SelectionTaskHistoryPO taskHistoryPO = new SelectionTaskHistoryPO();
        taskHistoryPO.setTaskId(taskId);
        taskHistoryPO.setStatus(status);
        taskHistoryPO.setSuccessMessage(StringUtils.EMPTY);
        taskHistoryPO.setErrorMessage(StringUtils.EMPTY);
        taskHistoryPO.setExecuteTime(System.currentTimeMillis());
        return taskHistoryPO;
    }

    /**
     * 设置缓存中的任务状态
     *
     * @param status 状态
     * @param task   task vo
     */
    private void setStatusToCache(short status, SelectionTaskVO task) {
        SelectionTaskVO cachedTask = localCache.getSelectionTaskCache().getIfPresent(task.getTaskId());
        if (null == cachedTask) {
            localCache.getSelectionTaskCache().put(task.getTaskId(), task);
        }
        cachedTask.setStatus(status);
    }

    /**
     * 获取任务
     *
     * @param taskId 任务id
     * @return task vo
     */
    private SelectionTaskVO loadTask(long taskId) {
        SelectionTaskPO taskPO = selectionTaskRepository.get(taskId);
        SelectionTaskHistoryPO taskHistoryPO = selectionTaskHistoryRepository.getLatestHistory(taskId);
        if (null == taskPO) {
            return null;
        }
        if (null == taskHistoryPO) {
            return SelectionTaskVO.builder()
                    .status(SelectionConstraints.TASK_STATE_UNKNOWN)
                    .taskId(taskId)
                    .executeTime(StringUtils.EMPTY)
                    .nextExecuteTime(StringUtils.EMPTY)
                    .configId(taskPO.getConfigId())
                    .errorMessage(SelectionTaskVO.Message.builder().build())
                    .successMessage(SelectionTaskVO.Message.builder().build())
                    .build();
        }
        SelectionConfigVO selectionConfigVO = JSONUtil.parseObject(taskPO.getConfig(), SelectionConfigVO.class);
        String cron = selectionConfigVO.getCron();
        return SelectionTaskVO.builder()
                .status(taskHistoryPO.getStatus())
                .taskId(taskId)
                .executeTime(DateFormatUtils.format(taskHistoryPO.getExecuteTime(), SelectionConstraints.DATE_FORMAT))
                .nextExecuteTime(DateFormatUtils.format(cronToCalendar(cron).getTime(), SelectionConstraints.DATE_FORMAT))
                .configId(taskPO.getConfigId())
                .errorMessage(SelectionTaskVO.Message.builder().code(taskHistoryPO.getCode()).message(taskHistoryPO.getErrorMessage()).build())
                .successMessage(SelectionTaskVO.Message.builder().code(taskHistoryPO.getCode()).message(taskHistoryPO.getSuccessMessage()).build())
                .build();
    }

    /**
     * 提交定时调度任务
     *
     * @param context  任务上下文
     * @param calendar calendar对象
     */
    private void submitScheduledTask(TaskContextVO context, Calendar calendar) {
        TimerUtil.executeTask(
                TimerUtil.Task.newBuilder()
                        .day(calendar.get(Calendar.DAY_OF_MONTH))
                        .hour(calendar.get(Calendar.HOUR_OF_DAY))
                        .minute(calendar.get(Calendar.MINUTE))
                        .second(calendar.get(Calendar.SECOND))
                        .runnable(new TimerUtil.TaskExecutor() {
                            @Override
                            public void run() {
                                try {
                                    SelectionProcessHandler.doProcess(context); //执行筛选
                                    log.info("{} submitScheduledTask-SelectionProcessHandler.doProcess success!", context.getCurrentThreadName());
                                } catch (Exception e) {
                                    log.error(context.getCurrentThreadName() + " submitScheduledTask-SelectionProcessHandler.doProcess error!", e);
                                    setStatusToCache(SelectionConstraints.TASK_STATE_ERROR, context.getTask());
                                    selectionTaskHistoryRepository.updateStatus(context.getTask().getTaskId(), SelectionConstraints.TASK_STATE_ERROR, e.getMessage(),
                                            e instanceof KaneRuntimeException ? SelectionConstraints.TASK_MESSAGE_CODE_BIZ_ERROR : SelectionConstraints.TASK_MESSAGE_CODE_BIZ_JAVA_ERROR);
                                } finally {
                                    SelectionSignalRegister.clear(context.getTask().getTaskId());
                                }
                            }
                        })
                        .executor(taskExecutor)
                        .threadName(context.getCurrentThreadName())
                        .build()
        );
    }

    /**
     * cron表达式转calendar对象
     *
     * @param cron cron表达式
     * @return calendar对象
     */
    private Calendar cronToCalendar(String cron) {
        CronTrigger trigger = new CronTrigger(cron);
        TriggerContext triggerContext = new SimpleTriggerContext();
        Date nextExecutionTime = trigger.nextExecutionTime(triggerContext);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextExecutionTime);
        return calendar;
    }

    /**
     * 构建taskPO
     *
     * @param selectionTaskVO   任务VO
     * @param selectionConfigVO 配置VO
     * @return 任务PO
     */
    private SelectionTaskPO buildTaskPO(SelectionTaskVO selectionTaskVO, SelectionConfigVO selectionConfigVO) {
        SelectionTaskPO taskPO = new SelectionTaskPO();
        taskPO.setConfig(JSONUtil.toJSONString(selectionConfigVO));
        taskPO.setConfigId(selectionTaskVO.getConfigId());
        taskPO.setId(selectionTaskVO.getTaskId());
        return taskPO;
    }

    /**
     * 构建工作线程名
     *
     * @param taskId 任务id
     * @return 线程名
     */
    private String buildThreadName(long taskId) {
        return SelectionConstraints.TASK_ID_PREFIX + taskId;
    }

    /**
     * 获取任务vo
     *
     * @param id 任务id
     * @return 任务vo对象
     */
    private SelectionTaskVO getSelectionTaskVO(long id) {
        SelectionTaskVO selectionTaskVO = getTask(id);
        if (null == selectionTaskVO) {
            throw new IllegalArgumentException("invalid task id");
        }
        return selectionTaskVO;
    }

    /**
     * 修改任务状态
     *
     * @param selectionTaskVO  任务vo
     * @param signalConstraint 信号
     * @param status           任务状态
     * @param code             状态码
     */
    private void optionTaskStatus(SelectionTaskVO selectionTaskVO, SelectionTaskSignalConstraint signalConstraint, short status, int code) {
        if (SelectionConstraints.isFinalFinalState(status)) { //不是最终状态
            return;
        }
        single(selectionTaskVO.getTaskId(), signalConstraint);//发信号暂停任务
        String message = StringUtils.EMPTY;
        String now = DateFormatUtils.format(new Date(), SelectionConstraints.DATE_FORMAT);
        if (signalConstraint.equals(SelectionTaskSignalConstraint.PAUSE)) {
            message = String.format(SelectionConstraints.TASK_MESSAGE_CODE_PAUSED_MESSAGE, now);
        } else if (signalConstraint.equals(SelectionTaskSignalConstraint.STOPPED)) {
            message = String.format(SelectionConstraints.TASK_MESSAGE_CODE_STOPPED_MESSAGE, now);
        } else if (signalConstraint.equals(SelectionTaskSignalConstraint.RESUME)) {
            message = StringUtils.EMPTY;
        }
        selectionTaskVO.setSuccessMessage(SelectionTaskVO.Message.builder()
                .code(code)
                .message(message)
                .build());
        setStatusToCache(status, selectionTaskVO);
        selectionTaskHistoryRepository.updateStatus(selectionTaskVO.getTaskId(), status, message, code);
    }
}
