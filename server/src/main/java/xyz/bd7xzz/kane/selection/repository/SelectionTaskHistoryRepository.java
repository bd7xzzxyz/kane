package xyz.bd7xzz.kane.selection.repository;

import xyz.bd7xzz.kane.po.SelectionTaskHistoryPO;

/**
 * @author baodi1
 * @description: 筛选任务历史落地
 * @date 2021/10/4 2:37 下午
 */
public interface SelectionTaskHistoryRepository {

    /**
     * 更新筛选状态
     *
     * @param taskId     筛选任务id
     * @param taskStatus 筛选任务状态
     * @param message    异常或成功信息
     * @param code       状态码
     */
    void updateStatus(long taskId, short taskStatus, String message,int code);

    /**
     * 保存
     *
     * @param taskHistoryPO 任务历史
     */
    void save(SelectionTaskHistoryPO taskHistoryPO);

    /**
     * 获取最后一条执行历史
     * @param taskId 筛选任务id
     * @return 筛选任务历史记录
     */
    SelectionTaskHistoryPO getLatestHistory(long taskId);
}
