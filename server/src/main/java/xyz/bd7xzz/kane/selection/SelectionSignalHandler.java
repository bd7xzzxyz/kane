package xyz.bd7xzz.kane.selection;

import xyz.bd7xzz.kane.vo.TaskContextVO;

public interface SelectionSignalHandler {

    /**
     * 信号处理
     * @param taskContext 任务上下文
     */
    boolean handle(TaskContextVO taskContext);
}
