package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.constraint.DataSourceTypeConstraint;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

@Service
public class DataSourceConfigFacade {
    private final DataSourceConfigManager dataSourceConfigManager;

    @Autowired
    public DataSourceConfigFacade(DataSourceConfigManager dataSourceConfigManager) {
        this.dataSourceConfigManager = dataSourceConfigManager;
    }

    /**
     * 创建数据源
     *
     * @param dataSourceConfigVO
     * @return
     */
    public ResponseVO createDataSource(DataSourceConfigVO dataSourceConfigVO) {
        long dataSourceId;
        if (DataSourceTypeConstraint.isRealTime(dataSourceConfigVO.getType())) {
            dataSourceId = dataSourceConfigManager.registerRealtimeDataSource(dataSourceConfigVO);
        } else {
            dataSourceId = dataSourceConfigManager.registerScheduleDataSource(dataSourceConfigVO);
        }
        return ResponseVO.buildSuccess(dataSourceId);
    }
}
