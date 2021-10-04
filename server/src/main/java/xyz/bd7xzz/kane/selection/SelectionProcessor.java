package xyz.bd7xzz.kane.selection;

import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 筛选处理器
 * @date 2021/8/24 2:50 下午
 */
public interface SelectionProcessor {
    /**
     * 执行处理器
     *
     * @param context 上下文
     */
    boolean doProcess(TaskContextVO context);
}
