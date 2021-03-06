package xyz.bd7xzz.kane.configmanager.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.configmanager.CollectionFieldManager;
import xyz.bd7xzz.kane.configmanager.repository.CollectionFieldRepository;
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.po.CollectionFieldPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.util.VersionUtil;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段服务
 * @date 7/14/21 10:04 PM
 */
@Service
@Slf4j
public class CollectionFieldManagerImpl implements CollectionFieldManager {

    private final CollectionFieldRepository collectionFieldRepository;
    private final SnowFlakeProperties snowFlakeProperties;
    private final LocalCache localCache;

    @Autowired
    public CollectionFieldManagerImpl(CollectionFieldRepository collectionFieldRepository, SnowFlakeProperties snowFlakeProperties, LocalCache localCache) {
        this.collectionFieldRepository = collectionFieldRepository;
        this.snowFlakeProperties = snowFlakeProperties;
        this.localCache = localCache;
    }

    @Override
    public List<Long> addCollectionField(List<CollectionFieldVO> collectionFields, List<DataSourceConfigVO> dataSourceConfigVOS) {
        if (CollectionUtils.isEmpty(dataSourceConfigVOS)) {
            throw new IllegalArgumentException("invalid data source id");
        }
        Map<Long, DataSourceConfigVO> mapper = dataSourceConfigVOS.stream().collect(Collectors.toMap(DataSourceConfigVO::getId, Function.identity()));
        collectionFields.forEach(collectionField -> {
            if (!mapper.containsKey(collectionField.getDataSourceId())) {
                throw new IllegalArgumentException("invalid data source id");
            }
        });

        List<Long> ids = batchGenerateIds(collectionFields.size());
        try {
            List<CollectionFieldPO> fieldPOS = buildFieldPO(ids, collectionFields);
            collectionFieldRepository.batchSave(fieldPOS);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("convertDataSourceConfigPO error when copy bean", e);
            throw new KaneRuntimeException("add collection field error");
        }
        return ids;
    }

    @Override
    public CollectionFieldVO getById(long id) {
        CollectionFieldPO collectionFieldPO = getCollectionField(id);
        try {
            return BeanUtil.copy(collectionFieldPO, CollectionFieldVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getById error when copy bean", e);
            throw new KaneRuntimeException("get collection field error");
        }
    }

    @Override
    public void deleteCollectionField(long id) {
        CollectionFieldPO collectionFieldPO = getCollectionField(id);
        localCache.getCollectionFieldCache().invalidate(collectionFieldPO.getDataSourceId());
        collectionFieldRepository.deleteCollectionField(id);
    }

    @Override
    public List<CollectionFieldVO> getCollectionFieldByDataSourceId(long dataSourceId) {
        List<CollectionFieldPO> collectionFieldPOS = collectionFieldRepository.getCollectionFieldByDataSourceId(dataSourceId);
        if (CollectionUtils.isEmpty(collectionFieldPOS)) {
            return Lists.newArrayListWithCapacity(0);
        }
        try {
            return BeanUtil.copy(collectionFieldPOS, CollectionFieldVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getCollectionFieldByDataSourceId error when copy bean", e);
            throw new KaneRuntimeException("get collection field error");
        }
    }

    @Override
    public void updateCollectionField(CollectionFieldVO collectionFieldVO, DataSourceConfigVO dataSourceConfigVO) {
        CollectionFieldPO collectionFieldPO = getCollectionField(collectionFieldVO.getId());
        CollectionFieldPO newField = convertPO(collectionFieldVO).diffAndSet(collectionFieldPO);
        localCache.getCollectionFieldCache().invalidate(dataSourceConfigVO.getId());
        collectionFieldRepository.updateCollectionField(newField);
    }

    /**
     * 转换采集字段vo
     *
     * @param collectionFieldVO 采集字段vo
     * @return 采集字段po
     */
    private CollectionFieldPO convertPO(CollectionFieldVO collectionFieldVO) {
        try {
            return BeanUtil.copy(collectionFieldVO, CollectionFieldPO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getCollectionFieldByDataSourceId error when copy bean", e);
            throw new KaneRuntimeException("get collection field error");
        }
    }

    /**
     * 按id获取采集字段
     *
     * @param id 采集字段id
     * @return 采集字段po
     */
    private CollectionFieldPO getCollectionField(long id) {
        CollectionFieldPO collectionFieldPO = collectionFieldRepository.getById(id);
        if (null == collectionFieldPO) {
            throw new IllegalArgumentException("invalid collection field id");
        }
        return collectionFieldPO;
    }

    /**
     * 构建采集字段po
     *
     * @param ids              采集字段id
     * @param collectionFields 采集字段vo
     * @return 采集字段po
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private List<CollectionFieldPO> buildFieldPO(List<Long> ids, List<CollectionFieldVO> collectionFields) throws IllegalAccessException, InstantiationException {
        String version = VersionUtil.generateVersion();
        List<CollectionFieldPO> fieldPOS = BeanUtil.copy(collectionFields, CollectionFieldPO.class);
        for (int i = 0; i < fieldPOS.size(); i++) {
            fieldPOS.get(i).setId(ids.get(i));
            fieldPOS.get(i).setVersion(version);
        }
        return fieldPOS;
    }

    /**
     * 生成一批id
     *
     * @param size 生成个数
     * @return id列表
     */
    private List<Long> batchGenerateIds(int size) {
        List<Long> results = Lists.newArrayListWithCapacity(size);
        for (int i = 0; i < size; i++) {
            long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
            results.add(id);
        }
        return results;
    }
}
