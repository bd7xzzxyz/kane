package xyz.bd7xzz.kane.configmanager.impl;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

@Service
public class DataSourceConfigManagerImpl implements DataSourceConfigManager {

    @Override
    public long registerRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO) {
        return 0;
    }

    @Override
    public long registerScheduleDataSource(DataSourceConfigVO dataSourceConfigVO) {
        return 0;
    }
}
