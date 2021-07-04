package xyz.bd7xzz.kane.configmanager.repository;

import xyz.bd7xzz.kane.po.DataSourceConfigPO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源配置落地
 * @date 7/4/21 11:57 AM
 */
public interface DataSourceConfigRepository {
    /**
     * 保存数据源配置
     *
     * @param configPO 数据源配置
     */
    void save(DataSourceConfigPO configPO);

    /**
     * 按id获取数据源配置
     *
     * @param id 数据源id
     * @return
     */
    DataSourceConfigPO get(long id);

    /**
     * 按id删除数据源配置
     *
     * @param id 数据源id
     */
    void delete(long id);
}
