package xyz.bd7xzz.kane.constraint;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EngineTypeConstraint {
    ELASTIC(1, "elastic search", "", ""),
    MYSQL(2, "mysql", "", ""),
    DORIS(3, "doris", "", "");
    private int type;
    private String name;
    private String dmlBeanName;
    private String dqlBeanName;

    private static final ImmutableMap<Integer, EngineTypeConstraint> CACHE;

    static {
        ImmutableMap.Builder<Integer, EngineTypeConstraint> builder = ImmutableMap.builder();
        for (EngineTypeConstraint value : EngineTypeConstraint.values()) {
            builder.put(value.getType(), value);
        }
        CACHE = builder.build();
    }

    /**
     * 根据类型获取dml 实现类名
     *
     * @param type 引擎类型
     * @return bean名
     */
    public static String getDMLBeanName(int type) {
        if (!CACHE.containsKey(type)) {
            throw new IllegalArgumentException("invalid engine type");
        }
        return CACHE.get(type).getDmlBeanName();
    }

    /**
     * 根据类型获取dql 实现类名
     *
     * @param type 引擎类型
     * @return bean名
     */
    public static String getDQLBeanName(int type) {
        if (!CACHE.containsKey(type)) {
            throw new IllegalArgumentException("invalid engine type");
        }
        return CACHE.get(type).getDqlBeanName();
    }
}
