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
    AUTO(0, "auto"), LONG(1, "long"), INT(2, "integer"), DOUBLE(3, "double"),
    FLOAT(4, "float"), CHAR(5, "character"), STRING(6, "string"), BOOL(7, "boolean");
    private int type;
    private String name;
}
