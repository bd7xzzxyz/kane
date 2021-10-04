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

    /**
     * 注册信号
     *
     * @param signalConstraint
     * @param taskId
     */
    public static void register(List<SelectionTaskSignalConstraint> signalConstraint, long taskId) {
        SIGNAL_MAPPER.put(taskId, signalConstraint);
    }

    /**
     * 执行信号处理函数
     *
     * @param taskContext
     * @return
     */
    public static boolean checkAndExecute(TaskContextVO taskContext) {
        List<SelectionTaskSignalConstraint> signals = SIGNAL_MAPPER.get(taskContext.getTask().getTaskId());
        for (SelectionTaskSignalConstraint signal : signals) {
            if (taskContext.getSignalFlag() != null && signal.equals(taskContext.getSignalFlag())) {
                return SpringContextUtil.getBean(signal.getHandlerName(), SelectionSignalHandler.class).handle(taskContext);
            }
        }
        return true;
    }
}
