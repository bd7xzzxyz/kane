package xyz.bd7xzz.kane.selection;


import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;
import xyz.bd7xzz.kane.vo.SelectionTaskVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选任务管理器
 * @date 7/25/21 10:51 PM
 */
public interface SelectionTaskManager {

    /**
     * 停止任务
     *
     * @param taskId 任务id
     */
    void stopTask(long taskId);

    /**
     * 根据配置id查找任务
     *
     * @param id SelectionConfig的id
     * @return 匹配到的所有任务id
     */
    List<Long> findTaskIdsBySelectionConfigId(long id);

    /**
     * 暂停任务
     *
     * @param taskId 任务id
     */
    void pauseTask(long taskId);

    /**
     * 恢复任务
     *
     * @param taskId 任务id
     */
    void resumeTask(long taskId);

    /**
     * 执行筛选
     *
     * @param id 筛选配置id
     * @return 筛选任务id
     */
    long executeSelection(long id);

    /**
     * 获取筛选任务
     *
     * @param taskId 任务Id
     * @return 筛选任务 SelectionTaskVO
     */
    SelectionTaskVO getTask(long taskId);

    /**
     * 向筛选任务发信号
     * @param taskId
     */
    void single(long taskId, SelectionTaskSignalConstraint signalConstraint);
}
