package xyz.bd7xzz.kane.configmanager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.configmanager.CollectionFieldManager;
import xyz.bd7xzz.kane.configmanager.LabelConfigManager;
import xyz.bd7xzz.kane.configmanager.repository.LabelConfigRepository;
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.po.LabelConfigPO;
import xyz.bd7xzz.kane.properties.SnowFlakeProperties;
import xyz.bd7xzz.kane.util.BeanUtil;
import xyz.bd7xzz.kane.util.SnowFlake;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.LabelConfigVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置服务
 * @date 7/18/21 3:46 PM
 */
@Service
@Slf4j
public class LabelConfigManagerImpl implements LabelConfigManager {

    private final LabelConfigRepository labelConfigRepository;
    private final SnowFlakeProperties snowFlakeProperties;
    private final CollectionFieldManager collectionFieldManager;

    @Autowired
    public LabelConfigManagerImpl(LabelConfigRepository labelConfigRepository,
                                  SnowFlakeProperties snowFlakeProperties,
                                  CollectionFieldManager collectionFieldManager) {
        this.labelConfigRepository = labelConfigRepository;
        this.snowFlakeProperties = snowFlakeProperties;
        this.collectionFieldManager = collectionFieldManager;
    }

    @Override
    public long createLabelConfig(LabelConfigVO labelConfigVO) {
        long id = SnowFlake.getId(snowFlakeProperties.getDataCenterId(), snowFlakeProperties.getMachineId());
        checkCollectionField(labelConfigVO.getFieldId());
        LabelConfigPO labelConfigPO;
        try {
            labelConfigPO = BeanUtil.copy(labelConfigVO, LabelConfigPO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("createLabelConfig error when copy bean", e);
            throw new KaneRuntimeException("add label error");
        }
        labelConfigPO.setId(id);
        labelConfigRepository.save(labelConfigPO);
        return id;
    }

    @Override
    public void deleteLabelConfig(long id) {
        getById(id);
        labelConfigRepository.deleteLabelConfig(id);
    }

    @Override
    public LabelConfigVO getLabelConfig(long id) {
        LabelConfigPO labelConfigPO = getById(id);
        try {
            return BeanUtil.copy(labelConfigPO, LabelConfigVO.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("getLabelConfig error when copy bean", e);
            throw new KaneRuntimeException("get label error");
        }
    }

    @Override
    public void updateLabelConfig(LabelConfigVO labelConfigVO) {
        LabelConfigPO oldPO = getById(labelConfigVO.getId());
        if (oldPO.getFieldId() != labelConfigVO.getFieldId()) {
            checkCollectionField(labelConfigVO.getFieldId());
        }

        try {
            LabelConfigPO labelConfigPO = BeanUtil.copy(labelConfigVO, LabelConfigPO.class).diffAndSet(oldPO);
            labelConfigRepository.updateLabelConfig(labelConfigPO);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("updateLabelConfig error when copy bean", e);
            throw new KaneRuntimeException("update label error");
        }
    }

    @Override
    public List<LabelConfigVO> listLabelConfig() {
        List<LabelConfigPO> labelConfigPOS = labelConfigRepository.listLabelConfig();
        try {
            return BeanUtil.copy(labelConfigPOS, LabelConfigVO.class);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("listLabelConfig error when copy bean", e);
            throw new KaneRuntimeException("list label error");
        }
    }

    /**
     * 按标签id获取标签
     *
     * @param id 标签id
     * @return 标签配置
     */
    private LabelConfigPO getById(long id) {
        LabelConfigPO labelConfigPO = labelConfigRepository.getById(id);
        if (null == labelConfigPO) {
            throw new IllegalArgumentException("invalid label id");
        }
        return labelConfigPO;
    }

    /**
     * 根据采集字段id获取采集字段
     *
     * @param fieldId 采集字段id
     */
    private void checkCollectionField(long fieldId) {
        CollectionFieldVO collectionFieldVO = collectionFieldManager.getById(fieldId);
        if (null == collectionFieldVO) {
            throw new IllegalArgumentException("invalid field id");
        }
    }
}
