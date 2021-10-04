package xyz.bd7xzz.kane.selection.impl;

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
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.po.SelectionTaskHistoryPO;
import xyz.bd7xzz.kane.po.SelectionTaskPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.selection.SelectionConfigManager;
import xyz.bd7xzz.kane.selection.SelectionProcessHandler;
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
    public List<SelectionTaskVO> findTasksBySelectionConfigId(long id) {
        //TODO

        return null;
    }

    @Override
    public void stopTask(long id) {
        //TODO

    }

    @Override
    public void pauseTask(long taskId) {
        //TODO
    }

    @Override
    public void resumeTask(long taskId) {
        //TODO

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
        SelectionTaskVO task = SelectionTaskVO.builder()
                .taskId(id)
                .configId(configId)
                .executeTime(DateFormatUtils.format(new Date(), SelectionConstraints.DATE_FORMAT))
                .build();
        TaskContextVO context = TaskContextVO.builder()
                .task(task)
                .selectionConfig(selectionConfigVO)
                .build();
        if (StringUtils.isNotEmpty(selectionConfigVO.getCron())) {
            Calendar calendar = cronToCalendar(selectionConfigVO.getCron());
            task.setStatus(SelectionConstraints.TASK_STATUS_WAITING);
            submitScheduledTask(context, calendar);
        } else {
            task.setStatus(SelectionConstraints.TASK_STATUS_RUNNING);
            taskExecutor.submitListenable(() -> {
                Thread.currentThread().setName(buildThreadName(id));
                SelectionProcessHandler.doProcess(context);
            }).addCallback(new ListenableFutureCallback<Object>() {
                @Override
                public void onSuccess(Object o) {
                    setStatusToCache(SelectionConstraints.TASK_STATUS_DONE, task);
                    selectionTaskHistoryRepository.updateStatus(id, SelectionConstraints.TASK_STATUS_DONE, o.toString(), SelectionConstraints.TASK_MESSAGE_CODE_SUCCESS);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    setStatusToCache(SelectionConstraints.TASK_STATUS_ERROR, task);
                    selectionTaskHistoryRepository.updateStatus(id, SelectionConstraints.TASK_STATUS_ERROR, throwable.getMessage(),
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
        SelectionTaskVO taskVO = localCache.getSelectionTaskCache().getUnchecked(taskId);
        if (null == taskVO) {
            taskVO = loadTask(taskId);
        }
        return taskVO;
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
        SelectionTaskVO cachedTask = localCache.getSelectionTaskCache().getUnchecked(task.getTaskId());
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
                    .status(SelectionConstraints.TASK_STATUS_UNKNOWN)
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
                                SelectionProcessHandler.doProcess(context);
                            }
                        })
                        .executor(taskExecutor)
                        .threadName(buildThreadName(context.getTask().getTaskId()))
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
}
