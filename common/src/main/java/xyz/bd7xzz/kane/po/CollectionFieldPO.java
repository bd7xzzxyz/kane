package xyz.bd7xzz.kane.po;

import lombok.Data;

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
}
