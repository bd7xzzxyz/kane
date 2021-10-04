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
     * @param selectionConfig 筛选配置
     * @return 配置id
     */
    long createSelection(SelectionConfigVO selectionConfig);

    /**
     * 更新筛选配置
     *
     * @param selectionConfig 筛选配置
     */
    void updateSelection(SelectionConfigVO selectionConfig);

    /**
     * 删除筛选配置
     *
     * @param id 配置id
     */
    void deleteSelection(long id);

    /**
     * 获取筛选配置
     *
     * @param id 配置id
     * @return 筛选配置
     */
    SelectionConfigVO getSelection(long id);

    /**
     * 从缓存中获取筛选配置
     * @param id 配置id
     * @return 筛选配置
     */
    SelectionConfigVO getSelectionFromCache(long id);
}
