package xyz.bd7xzz.kane.configmanager;

import xyz.bd7xzz.kane.vo.LabelConfigVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置服务
 * @date 7/18/21 3:45 PM
 */
public interface LabelConfigManager {

    /**
     * 创建标签
     *
     * @param labelConfigVO 标签配置
     * @return 标签id
     */
    long createLabelConfig(LabelConfigVO labelConfigVO);

    /**
     * 根据id删除标签
     *
     * @param id 标签id
     */
    void deleteLabelConfig(long id);

    /**
     * 获取标签配置
     *
     * @param id 标签id
     * @return 标签配置
     */
    LabelConfigVO getLabelConfig(long id);

    /**
     * 修改标签配置
     *
     * @param labelConfigVO 标签配置
     */
    void updateLabelConfig(LabelConfigVO labelConfigVO);

    /**
     * 获取所有标签
     *
     * @return 标签配置集合
     */
    List<LabelConfigVO> listLabelConfig();
}
