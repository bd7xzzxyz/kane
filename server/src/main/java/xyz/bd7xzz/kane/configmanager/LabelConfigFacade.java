package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.vo.LabelConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置门面
 * @date 7/18/21 3:44 PM
 */
@Service
public class LabelConfigFacade {

    private final LabelConfigManager labelConfigManager;

    @Autowired
    public LabelConfigFacade(LabelConfigManager labelConfigManager) {
        this.labelConfigManager = labelConfigManager;
    }

    /**
     * 创建标签
     *
     * @param labelConfigVO 标签配置
     * @return ResponseVO 标签id
     */
    public ResponseVO createLabelConfig(LabelConfigVO labelConfigVO) {
        long labelId = labelConfigManager.createLabelConfig(labelConfigVO);
        return ResponseVO.buildSuccess(labelId);
    }

    /**
     * 删除标签
     *
     * @param id 标签id
     * @return ResponseVO对象
     */
    public ResponseVO deleteLabelConfig(long id) {
        labelConfigManager.deleteLabelConfig(id);
        return ResponseVO.buildSuccess();
    }

    /**
     * 根据id获取标签
     *
     * @param id 标签id
     * @return ResponseVO对象 标签对象
     */
    public ResponseVO getLabelConfig(long id) {
        LabelConfigVO labelConfigVO = labelConfigManager.getLabelConfig(id);
        return ResponseVO.buildSuccess(labelConfigVO);
    }

    /**
     * 修改标签配置
     *
     * @param labelConfigVO 标签配置
     * @return ResponseVO对象
     */
    public ResponseVO updateLabelConfig(LabelConfigVO labelConfigVO) {
        labelConfigManager.updateLabelConfig(labelConfigVO);
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取所有标签
     *
     * @return ResponseVO对象 标签集合
     */
    public ResponseVO listLabelConfig() {
        List<LabelConfigVO> labelConfigVOS = labelConfigManager.listLabelConfig();
        return ResponseVO.buildSuccess(labelConfigVOS);
    }
}
