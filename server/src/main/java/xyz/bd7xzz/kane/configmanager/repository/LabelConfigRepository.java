package xyz.bd7xzz.kane.configmanager.repository;

import xyz.bd7xzz.kane.po.LabelConfigPO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置落地
 * @date 7/18/21 3:47 PM
 */
public interface LabelConfigRepository {
    /**
     * 保存标签
     *
     * @param labelConfigPO 标签配置
     */
    void save(LabelConfigPO labelConfigPO);

    /**
     * 按id获取标签
     *
     * @param id 标签id
     * @return 标签配置
     */
    LabelConfigPO getById(long id);

    /**
     * 删除标签
     *
     * @param id 标签id
     */
    void deleteLabelConfig(long id);

    /**
     * 更新标签
     *
     * @param labelConfigPO 标签配置
     */
    void updateLabelConfig(LabelConfigPO labelConfigPO);

    /**
     * 获取所有标签
     *
     * @return 标签集合
     */
    List<LabelConfigPO> listLabelConfig();
}
