package xyz.bd7xzz.kane.selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.vo.ResponseVO;
import xyz.bd7xzz.kane.vo.SelectionConfigVO;
import xyz.bd7xzz.kane.vo.SelectionTaskVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选门面
 * @date 7/25/21 10:49 PM
 */
@Service
public class SelectionFacade {

    private final SelectionTaskManager selectionTaskManager;
    private final SelectionConfigManager selectionConfigManager;

    @Autowired
    public SelectionFacade(SelectionTaskManager selectionTaskManager, SelectionConfigManager selectionConfigManager) {
        this.selectionTaskManager = selectionTaskManager;
        this.selectionConfigManager = selectionConfigManager;
    }

    /**
     * 创建筛选配置
     *
     * @param selectionConfig
     * @return 筛选配置的id
     */
    public ResponseVO createSelection(SelectionConfigVO selectionConfig) {
        long id = selectionConfigManager.createSelection(selectionConfig);
        return ResponseVO.buildSuccess(id);
    }

    /**
     * 更新筛选配置
     *
     * @param selectionConfig
     * @return 成功true 失败false
     */
    public ResponseVO updateSelection(SelectionConfigVO selectionConfig) {
        selectionConfigManager.updateSelection(selectionConfig);
        return ResponseVO.buildSuccess();
    }

    /**
     * 删除筛选配置
     *
     * @param id
     * @return 成功true 失败false
     */
    public ResponseVO deleteSelection(long id) {
        List<SelectionTaskVO> tasks = selectionTaskManager.findTasksBySelectionConfigId(id);
        tasks.forEach(selectionTaskVO -> selectionTaskManager.stopTask(id));
        selectionConfigManager.deleteSelection(id);
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取筛选配置
     *
     * @param id
     * @return SelectionConfigVO 配置项
     */
    public ResponseVO getSelection(long id) {
        SelectionConfigVO selectionConfigVO = selectionConfigManager.getSelection(id);
        return ResponseVO.buildSuccess(selectionConfigVO);
    }

    /**
     * 执行筛选
     *
     * @param id
     * @return 筛选任务id
     */
    public ResponseVO executeSelection(long id) {
        long taskId = selectionTaskManager.executeSelection(id);
        return ResponseVO.buildSuccess(taskId);
    }

    /**
     * 停止任务
     *
     * @param taskId
     * @return 成功true 失败false
     */
    public ResponseVO stopTask(long taskId) {
        selectionTaskManager.stopTask(taskId);
        return ResponseVO.buildSuccess();
    }

    /**
     * 暂停任务
     *
     * @param taskId
     * @return 成功true 失败false
     */
    public ResponseVO pauseTask(long taskId) {
        selectionTaskManager.pauseTask(taskId);
        return ResponseVO.buildSuccess();
    }

    /**
     * 恢复任务
     *
     * @param taskId
     * @return 成功true 失败false
     */
    public ResponseVO resumeTask(long taskId) {
        selectionTaskManager.resumeTask(taskId);
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取任务
     *
     * @param taskId 筛选任务Id
     * @return 筛选任务 SelectionTaskVO
     */
    public ResponseVO getTask(long taskId) {
        SelectionTaskVO selectionTaskVO = selectionTaskManager.getTask(taskId);
        return ResponseVO.buildSuccess(selectionTaskVO);
    }
}
