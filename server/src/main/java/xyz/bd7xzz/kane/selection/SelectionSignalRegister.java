package xyz.bd7xzz.kane.selection;

import com.google.common.collect.Maps;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;
import xyz.bd7xzz.kane.vo.TaskContextVO;

import java.util.List;
import java.util.Map;

/**
 * @author baodi1
 * @description: 信号管理器
 * @date 2021/10/4 6:33 下午
 */
public class SelectionSignalRegister {
    private static final Map<Long, List<SelectionTaskSignalConstraint>> SIGNAL_MAPPER = Maps.newConcurrentMap();
    private static final Map<Long, Thread> THREAD_MAP = Maps.newConcurrentMap();
    protected static final Map<Long, Object> MUTEX_MAP = Maps.newConcurrentMap();

    /**
     * 注册信号
     *
     * @param signalConstraint
     * @param taskId
     */
    public static void register(List<SelectionTaskSignalConstraint> signalConstraint, long taskId) {
        THREAD_MAP.put(taskId, Thread.currentThread());
        SIGNAL_MAPPER.put(taskId, signalConstraint);
        MUTEX_MAP.put(taskId, new Object());
    }

    /**
     * 执行信号处理函数
     *
     * @param taskContext
     * @return
     */
    public static boolean checkAndExecute(TaskContextVO taskContext, boolean isAopAround) {
        List<SelectionTaskSignalConstraint> signals = SIGNAL_MAPPER.get(taskContext.getTask().getTaskId());
        for (SelectionTaskSignalConstraint signal : signals) {
            if (isAopAround && taskContext.getSignalFlag() != null && signal.equals(taskContext.getSignalFlag())) { //aop around信号处理
                return SpringContextUtil.getBean(signal.getHandlerName(), SelectionSignalHandler.class).handleAround(taskContext);
            } else if (taskContext.getSignalFlag() != null && signal.equals(taskContext.getSignalFlag())) { //非aop信号处理
                SpringContextUtil.getBean(signal.getHandlerName(), SelectionSignalHandler.class).handle(taskContext);
            }
        }
        return true;
    }

    /**
     * 获取互斥量
     *
     * @param taskId 任务id
     * @return 互斥量
     */
    public static Object getMutex(long taskId) {
        return MUTEX_MAP.get(taskId);
    }

    /**
     * 获取任务执行线程
     *
     * @param taskId 线程id
     * @return 线程
     */
    public static Thread getThread(long taskId) {
        return THREAD_MAP.get(taskId);
    }

    /**
     * 清理注册器
     *
     * @param taskId 任务id
     */
    public static void clear(long taskId) {
        SIGNAL_MAPPER.remove(taskId);
        THREAD_MAP.remove(taskId);
        MUTEX_MAP.remove(taskId);
    }
}
