package xyz.bd7xzz.kane.selection.repository;

import xyz.bd7xzz.kane.po.SelectionTaskPO;

import java.util.List;

/**
 * @author baodi1
 * @description: 筛选任务落地
 * @date 2021/8/24 4:06 下午
 */
public interface SelectionTaskRepository {

    /**
     * 保存任务
     *
     * @param taskPO 任务
     */
    void save(SelectionTaskPO taskPO);

    /**
     * 获取任务
     *
     * @param taskId 任务id
     * @return 任务
     */
    SelectionTaskPO get(long taskId);

    /**
     * 根据配置id获取筛选任务id
     *
     * @param id 筛选配置id
     * @return 关联的筛选任务id
     */
    List<Long> getTaskIdByConfigId(long id);
}
