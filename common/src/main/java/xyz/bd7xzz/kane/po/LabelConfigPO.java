package xyz.bd7xzz.kane.po;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置
 * @date 7/18/21 3:50 PM
 */
@Data
public class LabelConfigPO {
    private long id;
    private String name;
    private long fieldId;
    private String comment;
    private String dimension;

    /**
     * 处理新对象字段空值
     *
     * @param oldLabel 旧对象
     * @return 新对象
     */
    public LabelConfigPO diffAndSet(LabelConfigPO oldLabel) {
        if (id <= 0) {
            id = oldLabel.getId();
        }
        if (StringUtils.isEmpty(name)) {
            name = oldLabel.getName();
        }
        if (fieldId <= 0) {
            fieldId = oldLabel.getFieldId();
        }
        if (StringUtils.isEmpty(comment)) {
            comment = oldLabel.getComment();
        }
        if (StringUtils.isEmpty(dimension)) {
            dimension = oldLabel.getDimension();
        }
        return this;
    }
}
