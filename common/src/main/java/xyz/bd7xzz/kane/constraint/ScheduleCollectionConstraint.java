package xyz.bd7xzz.kane.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bd7xzz
 * @description: 调度类型的数据采集
 * @date 2021/7/8 7:03 下午
 */
@Getter
@AllArgsConstructor
public enum ScheduleCollectionConstraint {
    SCHEDULE_HTTP_COLLECTION(DataSourceTypeConstraint.SCHEDULE_HTTP_COLLECTION.getType(), "httpCollectionServiceImpl");
    private int dataSourceType;
    private String beanName;

    /**
     * 按数据源类型获取bean名
     *
     * @param type 数据源类型
     * @return
     */
    public static String getBeanName(int type) {
        for (ScheduleCollectionConstraint value : ScheduleCollectionConstraint.values()) {
            if (value.dataSourceType == type) {
                return value.beanName;
            }
        }
        throw new IllegalArgumentException("invalid data source type:" + type);
    }
}

