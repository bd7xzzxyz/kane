package xyz.bd7xzz.kane.configmanager;

import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源配置服务
 * @date 7/14/21 10:00 PM
 */
public interface DataSourceConfigManager {

    /**
     * 注册实时数据源
     *
     * @param dataSourceConfigVO 数据源配置VO
     * @return 数据源id
     */
    long registerRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO);

    /**
     * 注册调度数据源
     *
     * @param dataSourceConfigVO 数据源配置VO
     * @return 数据源id
     */
    long registerScheduleDataSource(DataSourceConfigVO dataSourceConfigVO);

    /**
     * 更新实时数据源
     *
     * @param dataSourceConfigVO 数据源配置VO
     */
    void updateRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO);

    /**
     * 更新调度数据源
     *
     * @param dataSourceConfigVO 数据源配置VO
     */
    void updateScheduleDataSource(DataSourceConfigVO dataSourceConfigVO);

    /**
     * 根据id获取数据源
     *
     * @param id 数据源id
     * @return 数据源配置VO
     */
    DataSourceConfigVO getDataSourceById(long id);

    /**
     * 按id删除数据源
     *
     * @param id 数据源id
     */
    void deleteDataSource(long id);

    /**
     * 获取所有数据源
     *
     * @return 数据源配置VO
     */
    List<DataSourceConfigVO> listDataSource();

    /**
     * 批量根据id获取数据源
     *
     * @param ids 数据源id
     * @return 数据源配置VO
     */
    List<DataSourceConfigVO> batchGetDataSourceById(List<Long> ids);
}
