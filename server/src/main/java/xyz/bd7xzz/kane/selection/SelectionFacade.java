package xyz.bd7xzz.kane.selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
