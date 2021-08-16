package xyz.bd7xzz.kane.selection.repository;

import xyz.bd7xzz.kane.po.SelectionConfigPO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置落地
 * @date 7/25/21 10:46 PM
 */
public interface SelectionConfigRepository {
    SelectionConfigPO getSelectionPO(long id);

    /**
     * 保存筛选配置
     *
     * @param selectionConfigPO 筛选配置
     */
    void save(SelectionConfigPO selectionConfigPO);

    /**
     * 删除筛选配置
     *
     * @param id 配置id
     */
    void delete(long id);

    /**
     * 更新筛选配置
     *
     * @param configPO 筛选配置
     */
    void update(SelectionConfigPO configPO);
}
