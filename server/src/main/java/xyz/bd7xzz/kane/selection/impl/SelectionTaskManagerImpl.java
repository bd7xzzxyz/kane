package xyz.bd7xzz.kane.selection.impl;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.selection.SelectionTaskManager;
import xyz.bd7xzz.kane.vo.SelectionTaskVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选任务管理器
 * @date 7/25/21 10:52 PM
 */
@Service
public class SelectionTaskManagerImpl implements SelectionTaskManager {

    @Override
    public void stopTask(long id) {

    }

    @Override
    public List<SelectionTaskVO> findTasksBySelectionConfigId(long id) {
        return null;
    }

    @Override
    public void pauseTask(long taskId) {

    }

    @Override
    public void resumeTask(long taskId) {

    }

    @Override
    public long executeSelection(long id) {
        return 0;
    }

    @Override
    public SelectionTaskVO getTask(long taskId) {
        return null;
    }
}
