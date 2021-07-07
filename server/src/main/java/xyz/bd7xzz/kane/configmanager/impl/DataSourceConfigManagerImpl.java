package xyz.bd7xzz.kane.configmanager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.configmanager.ScheduledService;
import xyz.bd7xzz.kane.configmanager.repository.DataSourceConfigRepository;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.exception.KaneRuntimException;
import xyz.bd7xzz.kane.po.DataSourceConfigPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.util.VersionUtil;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

import java.util.List;

@Slf4j
@Service
public class DataSourceConfigManagerImpl implements DataSourceConfigManager {

    private final SnowFlakeProperties snowFlakeProperties;
    private final DataSourceConfigRepository dataSourceConfigRepository;
    private final ScheduledService scheduledService;

    @Autowired
    public DataSourceConfigManagerImpl(SnowFlakeProperties snowFlakeProperties,
                                       DataSourceConfigRepository dataSourceConfigRepository,
                                       ScheduledService scheduledService) {
        this.snowFlakeProperties = snowFlakeProperties;
        this.dataSourceConfigRepository = dataSourceConfigRepository;
        this.scheduledService = scheduledService;
    }

    @Override
    public long registerRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO) {
        checkDriverValid(dataSourceConfigVO);

        Class<? extends BasicDriverVO> driverVOClass = DataSourceDriverConstraint.getVOClass(dataSourceConfigVO.getType());
        BasicDriverVO driverVO = JSONUtil.parseObject(dataSourceConfigVO.getDriver(), driverVOClass);
        long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
        String version = VersionUtil.generateVersion();
        driverVO.setId(id);
        driverVO.setVersion(version);

        DataSourceConfigPO configPO = convertDataSourceConfigPO(dataSourceConfigVO);
        configPO.setId(id);
        configPO.setVersion(version);
        dataSourceConfigRepository.save(configPO);

        ConnectionHandler.createConnection(dataSourceConfigVO.getType(), driverVO);
        return id;
    }


    @Override
    public long registerScheduleDataSource(DataSourceConfigVO dataSourceConfigVO) {
        //TODO
        return 0;
    }

    @Override
    public void updateRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO) {
        checkDriverValid(dataSourceConfigVO);
        DataSourceConfigPO dataSourceConfigPO = getDataSourceConfigPO(dataSourceConfigVO.getId());
        DataSourceConfigPO newConfig = convertDataSourceConfigPO(dataSourceConfigVO);
        String version = VersionUtil.generateVersion();
        newConfig.setVersion(version);
        dataSourceConfigRepository.update(newConfig);
        if (dataSourceConfigPO.getType() != dataSourceConfigVO.getType() || !dataSourceConfigPO.getDriver().equals(dataSourceConfigVO.getDriver())) {
            Class<? extends BasicDriverVO> driverVOClass = DataSourceDriverConstraint.getVOClass(dataSourceConfigVO.getType());
            BasicDriverVO driverVO = JSONUtil.parseObject(dataSourceConfigVO.getDriver(), driverVOClass);
            driverVO.setId(dataSourceConfigVO.getId());
            driverVO.setVersion(version);
            ConnectionHandler.destroyConnection(dataSourceConfigVO.getId());
            ConnectionHandler.createConnection(dataSourceConfigVO.getType(), driverVO);
        }
    }

    @Override
    public void updateScheduleDataSource(DataSourceConfigVO dataSourceConfigVO) {
        //TODO
    }

    @Override
    public DataSourceConfigVO getDataSourceById(long id) {
        DataSourceConfigPO dataSourceConfigPO = getDataSourceConfigPO(id);
        try {
            return BeanUtil.copy(dataSourceConfigPO, DataSourceConfigVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getDataSourceById error when copy bean", e);
            throw new KaneRuntimException("get data source error");
        }
    }

    @Override
    public void deleteDataSource(long id) {
        getDataSourceConfigPO(id);
        dataSourceConfigRepository.delete(id);
        ConnectionHandler.destroyConnection(id);
    }

    @Override
    public List<DataSourceConfigVO> listDataSource() {
        List<DataSourceConfigPO> dataSourceConfigPOS = dataSourceConfigRepository.listDataSource();
        try {
            return BeanUtil.copy(dataSourceConfigPOS, DataSourceConfigVO.class);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("listDataSource error when copy bean", e);
            throw new KaneRuntimException("get data source error");
        }
    }

    /**
     * 转换数据源配置vo->po
     *
     * @param configVO 数据源配置vo
     * @return 数据源配置po
     */
    private DataSourceConfigPO convertDataSourceConfigPO(DataSourceConfigVO configVO) {
        DataSourceConfigPO configPO;
        try {
            configPO = BeanUtil.copy(configVO, DataSourceConfigPO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("convertDataSourceConfigPO error when copy bean", e);
            throw new KaneRuntimException("register or update real-time data source error");
        }
        return configPO;
    }

    /**
     * 校验驱动是否有效
     *
     * @param dataSourceConfigVO 数据源配置vo
     */
    private void checkDriverValid(DataSourceConfigVO dataSourceConfigVO) {
        if (JSONUtil.validate(dataSourceConfigVO.getDriver())) {
            throw new IllegalArgumentException("invalid driver json");
        }
    }

    /**
     * 按id获取数据源配置
     *
     * @param id 数据源id
     * @return 数据源配置对象
     */
    private DataSourceConfigPO getDataSourceConfigPO(long id) {
        DataSourceConfigPO dataSourceConfigPO = dataSourceConfigRepository.get(id);
        if (null == dataSourceConfigPO) {
            throw new IllegalArgumentException("invalid data source id");
        }
        return dataSourceConfigPO;
    }

}
