package xyz.bd7xzz.kane.configmanager.impl;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

import java.util.List;

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

    @Override
    public void updateRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO) {

    }

    @Override
    public void updateScheduleDataSource(DataSourceConfigVO dataSourceConfigVO) {

    }

    @Override
    public DataSourceConfigVO getDataSourceById(long id) {
        return null;
    }

    @Override
    public void deleteDataSource(long id) {

    }

    @Override
    public List<DataSourceConfigVO> listDataSource() {
        return null;
    }
}
