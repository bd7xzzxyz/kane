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
    SHORT(1, "short") {
        @Override
        public boolean equals(int type) {
            return type == SHORT.type;
        }
    }, LONG(2, "long") {
        @Override
        public boolean equals(int type) {
            return type == LONG.type;
        }
    }, INT(3, "integer") {
        @Override
        public boolean equals(int type) {
            return type == INT.type;
        }
    }, DOUBLE(4, "double") {
        @Override
        public boolean equals(int type) {
            return type == DOUBLE.type;
        }
    },
    FLOAT(5, "float") {
        @Override
        public boolean equals(int type) {
            return type == FLOAT.type;
        }
    }, CHAR(6, "character") {
        @Override
        public boolean equals(int type) {
            return type == CHAR.type;
        }
    }, STRING(7, "string") {
        @Override
        public boolean equals(int type) {
            return type == STRING.type;
        }
    }, BOOL(8, "boolean") {
        @Override
        public boolean equals(int type) {
            return type == BOOL.type;
        }
    }, BYTE(9, "byte") {
        @Override
        public boolean equals(int type) {
            return type == BYTE.type;
        }
    };

    private int type;
    private String name;

    /**
     * 判断是否与指定的类型相同
     *
     * @param type 字段类型
     * @return true为是 false为否
     */
    public abstract boolean equals(int type);
}
