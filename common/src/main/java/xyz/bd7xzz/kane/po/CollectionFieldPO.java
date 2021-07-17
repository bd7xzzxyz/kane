package xyz.bd7xzz.kane.po;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段
 * @date 7/15/21 9:43 PM
 */
@Data
public class CollectionFieldPO {
    private long id;
    private long dataSourceId;
    private String name;
    private String sourceField;
    private String targetField;
    private int type;
    private String comment;
    private String version;

    /**
     * 处理新对象字段空值
     *
     * @param oldField 旧字段
     * @return 新对象
     */
    public CollectionFieldPO diffAndSet(CollectionFieldPO oldField) {
        if (id <= 0) {
            id = oldField.getId();
        }
        if (dataSourceId <= 0) {
            id = oldField.getDataSourceId();
        }
        if (StringUtils.isEmpty(name)) {
            name = oldField.getName();
        }
        if (StringUtils.isEmpty(sourceField)) {
            sourceField = oldField.getSourceField();
        }
        if (StringUtils.isEmpty(targetField)) {
            targetField = oldField.getTargetField();
        }
        if (type <= 0) {
            type = oldField.getType();
        }
        if (StringUtils.isEmpty(comment)) {
            comment = oldField.getComment();
        }
        return this;
    }
}
