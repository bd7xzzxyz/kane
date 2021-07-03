package xyz.bd7xzz.kane.constraint;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum DataSourceTypeConstraint {
    REAL_TIME_MQ(1, true, "MQ实时上报数据源"),
    REAL_TIME_HTTP(2, true, "http实时上报数据源"),
    REAL_TIME_HTTP_COLLECTION(3, true, "http实时采集数据源"),
    SCHEDULE_HTTP(4, false, "http定时上报数据源"),
    SCHEDULE_HTTP_COLLECTION(5, false, "http定时采集数据源");
    private int type;
    private boolean realTime;
    private String name;

    private static final Map<Integer, DataSourceTypeConstraint> MAPPER;

    static {
        ImmutableMap.Builder<Integer, DataSourceTypeConstraint> builder = ImmutableMap.builder();
        for (DataSourceTypeConstraint value : DataSourceTypeConstraint.values()) {
            builder.put(value.type, value);
        }
        MAPPER = builder.build();
    }

    /**
     * 是否是实时
     *
     * @param type
     * @return
     */
    public static boolean isRealTime(int type) {
        if (MAPPER.containsKey(type)) {
            return MAPPER.get(type).isRealTime();
        }
        throw new IllegalArgumentException("unknown data source type " + type);
    }
}
