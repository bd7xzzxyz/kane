package xyz.bd7xzz.kane.vo;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段
 * @date 7/14/21 9:07 PM
 */
@Data
public class CollectionFieldVO {
    private long id;
    private long dataSourceId;
    private String name;
    private String sourceField;
    private String targetField;
    private int type;
    private boolean primaryKey;
    private String comment;
    private String version;
}
