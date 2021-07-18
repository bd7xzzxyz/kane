package xyz.bd7xzz.kane.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 请求方法
 * @date 7/18/21 12:06 PM
 */
@AllArgsConstructor
@Getter
public enum CollectionRequestMethodConstraint {
    INSERT(1, "insert") {
        @Override
        public boolean equals(int type) {
            return type == INSERT.getType();
        }
    }, DELETE(2, "delete") {
        @Override
        public boolean equals(int type) {
            return type == DELETE.getType();
        }
    }, UPDATE(3, "update") {
        @Override
        public boolean equals(int type) {
            return type == UPDATE.getType();
        }
    };
    public static final String REQUEST_METHOD_KEY = "$.method";
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
