package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.constraint.DataSourceTypeConstraint;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

import java.util.List;

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
     * @param dataSourceConfigVO 数据源配置VO
     * @return ResponseVO data 创建后的数据源id
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

    /**
     * 更新数据源
     *
     * @param dataSourceConfigVO 数据源配置VO
     * @return ResponseVO
     */
    public ResponseVO updateDataSource(DataSourceConfigVO dataSourceConfigVO) {
        if (DataSourceTypeConstraint.isRealTime(dataSourceConfigVO.getType())) {
            dataSourceConfigManager.updateRealtimeDataSource(dataSourceConfigVO);
        } else {
            dataSourceConfigManager.updateScheduleDataSource(dataSourceConfigVO);
        }
        return ResponseVO.buildSuccess();
    }

    /**
     * 根据id获取数据源
     *
     * @param id 数据源id
     * @return ResponseVO data 数据源配置
     */
    public ResponseVO getDataSource(long id) {
        DataSourceConfigVO dataSourceConfigVO = dataSourceConfigManager.getDataSourceById(id);
        return ResponseVO.buildSuccess(dataSourceConfigVO);
    }

    /**
     * 根据id删除数据源
     *
     * @param id 数据源id
     * @return ResponseVO
     */
    public ResponseVO deleteDataSource(long id) {
        dataSourceConfigManager.deleteDataSource(id);
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取所有数据源
     *
     * @return ResponseVO data 数据源配置
     */
    public ResponseVO listDataSource() {
        List<DataSourceConfigVO> configVOList = dataSourceConfigManager.listDataSource();
        return ResponseVO.buildSuccess(configVOList);
    }
}
