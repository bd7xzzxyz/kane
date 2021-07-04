package xyz.bd7xzz.kane.configmanager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.configmanager.DriverHandler;
import xyz.bd7xzz.kane.configmanager.repository.DataSourceConfigRepository;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.exception.KaneRuntimException;
import xyz.bd7xzz.kane.po.DataSourceConfigPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DataSourceConfigManagerImpl implements DataSourceConfigManager {

    private final SnowFlakeProperties snowFlakeProperties;
    private final DataSourceConfigRepository dataSourceConfigRepository;
    private final LocalCache localCache;

    @Autowired
    public DataSourceConfigManagerImpl(SnowFlakeProperties snowFlakeProperties, DataSourceConfigRepository dataSourceConfigRepository, LocalCache localCache) {
        this.snowFlakeProperties = snowFlakeProperties;
        this.dataSourceConfigRepository = dataSourceConfigRepository;
        this.localCache = localCache;
    }

    @Override
    public long registerRealtimeDataSource(DataSourceConfigVO dataSourceConfigVO) {
        if (JSONUtil.validate(dataSourceConfigVO.getDriver())) {
            throw new IllegalArgumentException("invalid driver json");
        }

        Class<? extends BasicDriverVO> driverVOClass = DataSourceDriverConstraint.getVOClass(dataSourceConfigVO.getType());
        BasicDriverVO driverVO = JSONUtil.parseObject(dataSourceConfigVO.getDriver(), driverVOClass);
        long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
        driverVO.setId(id);
        driverVO.setVersion(UUID.randomUUID().toString());

        DataSourceConfigPO configPO;
        try {
            configPO = BeanUtil.copy(driverVO, DataSourceConfigPO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("registerRealtimeDataSource error when copy bean", e);
            throw new KaneRuntimException("register real-time data source error");
        }
        dataSourceConfigRepository.save(configPO);

        DriverHandler.handle(dataSourceConfigVO.getType(), driverVO);
        return id;
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
        ConcurrentMessageListenerContainer container = localCache.getKafkaListenerCache().getIfPresent(id);
        if (null == container) {
            return;
        }
        container.stop(true);
        localCache.getKafkaListenerCache().invalidate(id);
    }

    @Override
    public List<DataSourceConfigVO> listDataSource() {
        return null;
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
