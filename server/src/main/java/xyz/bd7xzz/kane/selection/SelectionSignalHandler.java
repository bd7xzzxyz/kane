package xyz.bd7xzz.kane.selection;

import xyz.bd7xzz.kane.vo.TaskContextVO;


public interface SelectionSignalHandler {

    /**
     * Aop around信号处理
     *
     * @param taskContext 任务上下文
     * @return true继续执行 false 停止执行
     */
    boolean handleAround(TaskContextVO taskContext);

    /**
     * 信号处理
     *
     * @param taskContext 任务上下文
     */
    void handle(TaskContextVO taskContext);

}
