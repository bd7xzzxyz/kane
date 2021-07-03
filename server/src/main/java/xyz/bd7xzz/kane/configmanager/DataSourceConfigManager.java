package xyz.bd7xzz.kane.configmanager;

import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

public interface DataSourceConfigManager {

    /**
     * 注册实时数据源
     *
     * @param dataSourceConfigVO
     */
    long registerRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO);

    /**
     * 注册调度数据源
     * @param dataSourceConfigVO
     * @return
     */
    long registerScheduleDataSource(DataSourceConfigVO dataSourceConfigVO);
}
