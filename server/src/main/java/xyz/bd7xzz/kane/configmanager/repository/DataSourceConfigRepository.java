package xyz.bd7xzz.kane.configmanager.repository;

import xyz.bd7xzz.kane.po.DataSourceConfigPO;

import java.util.List;

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

    /**
     * 更新数据源配置
     *
     * @param configPO 数据源配置
     */
    void update(DataSourceConfigPO configPO);

    /**
     * 获取所有数据源配置
     *
     * @return 数据源配置
     */
    List<DataSourceConfigPO> listDataSource();

    /**
     * 根据多个id获取数据源
     * @param ids id列表
     * @return 数据源配置
     */
    List<DataSourceConfigPO> batchGetDataSourceById(List<Long> ids);
}
