package xyz.bd7xzz.kane.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段
 * @date 7/14/21 9:11 PM
 */
@Getter
@AllArgsConstructor
public enum CollectionFieldTypeConstraint {
    AUTO(1, "auto"), LONG(2, "long"), INT(3, "integer"), DOUBLE(4, "double"),
    FLOAT(5, "float"), CHAR(6, "character"), STRING(7, "string"), BOOL(8, "boolean");
    private int type;
    private String name;
}
