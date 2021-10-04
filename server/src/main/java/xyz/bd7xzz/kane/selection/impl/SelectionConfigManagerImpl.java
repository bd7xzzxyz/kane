package xyz.bd7xzz.kane.selection.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.po.SelectionConfigPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.selection.SelectionConfigManager;
import xyz.bd7xzz.kane.selection.repository.SelectionConfigRepository;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.util.VersionUtil;
import xyz.bd7xzz.kane.vo.SelectionConfigVO;

import java.util.concurrent.ExecutionException;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置管理器
 * @date 7/25/21 10:53 PM
 */
@Service
@Slf4j
public class SelectionConfigManagerImpl implements SelectionConfigManager {

    private final SelectionConfigRepository configRepository;
    private final SnowFlakeProperties snowFlakeProperties;
    private final LocalCache localCache;

    public SelectionConfigManagerImpl(SelectionConfigRepository configRepository, SnowFlakeProperties snowFlakeProperties, LocalCache localCache) {
        this.configRepository = configRepository;
        this.snowFlakeProperties = snowFlakeProperties;
        this.localCache = localCache;
    }

    @Override
    public long createSelection(SelectionConfigVO selectionConfig) {
        long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
        SelectionConfigPO selectionConfigPO = buildConfigPO(selectionConfig);
        selectionConfigPO.setId(id);
        selectionConfigPO.setVersion(VersionUtil.generateVersion());
        configRepository.save(selectionConfigPO);
        localCache.getSelectionConfigCache().put(id, selectionConfig);
        return id;
    }

    @Override
    public void updateSelection(SelectionConfigVO selectionConfig) {
        SelectionConfigPO oldConfig = getSelectionPO(selectionConfig.getId());
        SelectionConfigPO configPO = buildConfigPO(selectionConfig).diffAndSet(oldConfig);
        configPO.setVersion(VersionUtil.generateVersion());
        localCache.getSelectionConfigCache().invalidate(selectionConfig.getId());
        configRepository.update(configPO);
    }

    @Override
    public void deleteSelection(long id) {
        getSelectionPO(id);
        localCache.getSelectionConfigCache().invalidate(id);
        configRepository.delete(id);
    }

    @Override
    public SelectionConfigVO getSelection(long id) {
        SelectionConfigPO configPO = getSelectionPO(id);
        try {
            return BeanUtil.copy(configPO, SelectionConfigVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getSelection error!", e);
            throw new KaneRuntimeException("get selection config error");
        }
    }

    @Override
    public SelectionConfigVO getSelectionFromCache(long id) {
        try {
            return localCache.getSelectionConfigCache().get(id);
        } catch (ExecutionException e) {
            log.error("getSelectionFromCache error!", e);
            throw new KaneRuntimeException("get selection config from cache error");
        }
    }

    /**
     * 根据配置vo构建一个po对象
     *
     * @param selectionConfig 配置vo
     * @return 配置po对象
     */
    private SelectionConfigPO buildConfigPO(SelectionConfigVO selectionConfig) {
        SelectionConfigPO selectionConfigPO = new SelectionConfigPO();
        selectionConfigPO.setCron(selectionConfig.getCron());
        if (CollectionUtils.isEmpty(selectionConfig.getConditions())) {
            throw new IllegalArgumentException("invalid conditions");
        }
        selectionConfigPO.setConditions(JSONUtil.toJSONString(selectionConfig.getConditions()));
        if (null == selectionConfig.getConnection()) {
            throw new IllegalArgumentException("invalid connection");
        }
        selectionConfigPO.setConnection(JSONUtil.toJSONString(selectionConfig.getConnection()));
        selectionConfigPO.setName(selectionConfig.getName());
        selectionConfigPO.setRelation(selectionConfig.getRelation());
        return selectionConfigPO;
    }

    /**
     * 根据配置Id获取筛选配置
     *
     * @param id 筛选配置Id
     * @return 筛选配置PO对象
     */
    private SelectionConfigPO getSelectionPO(long id) {
        SelectionConfigPO selectionConfigPO = configRepository.get(id);
        if (null == selectionConfigPO) {
            throw new IllegalArgumentException("invalid selection config id");
        }
        return selectionConfigPO;
    }
}
