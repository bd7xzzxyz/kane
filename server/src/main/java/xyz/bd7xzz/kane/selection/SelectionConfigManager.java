package xyz.bd7xzz.kane.selection;

import xyz.bd7xzz.kane.vo.SelectionConfigVO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置管理器
 * @date 7/25/21 10:53 PM
 */
public interface SelectionConfigManager {

    /**
     * 创建筛选配置
     *
     * @param selectionConfig
     * @return
     */
    long createSelection(SelectionConfigVO selectionConfig);

    /**
     * 更新筛选配置
     *
     * @param selectionConfig
     */
    void updateSelection(SelectionConfigVO selectionConfig);

    /**
     * 删除筛选配置
     *
     * @param id
     */
    void deleteSelection(long id);

    /**
     * 获取筛选配置
     *
     * @param id
     * @return
     */
    SelectionConfigVO getSelection(long id);
}
