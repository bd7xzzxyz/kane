package xyz.bd7xzz.kane.constraint;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;
import xyz.bd7xzz.kane.vo.driver.KafkaDriverVO;

import java.util.Map;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源驱动
 * @date 7/4/21 1:12 PM
 */
@Getter
@AllArgsConstructor
public enum DataSourceDriverConstraint {

    KAFKA(1, KafkaDriverVO.class, "kafkaDriverHandler");
    private int type;
    private Class<? extends BasicDriverVO> voClazz;
    private String beanName;

    private static final Map<Integer, DataSourceDriverConstraint> MAPPER;

    static {
        ImmutableMap.Builder<Integer, DataSourceDriverConstraint> builder = ImmutableMap.builder();
        for (DataSourceDriverConstraint value : DataSourceDriverConstraint.values()) {
            builder.put(value.type, value);
        }
        MAPPER = builder.build();
    }

    /**
     * 获取数据源vo类
     *
     * @param type 数据源类型
     * @return 驱动vo
     */
    public static Class<? extends BasicDriverVO> getVOClass(int type) {
        Class<? extends BasicDriverVO> clazz = MAPPER.get(type).getVoClazz();
        if (null == clazz) {
            throw new IllegalArgumentException("invalid driver type");
        }
        return clazz;
    }

    /**
     * 获取spring bean名
     *
     * @param type 数据源类型
     * @return spring bean名
     */
    public static String getBeanName(int type) {
        String beanName = MAPPER.get(type).getBeanName();
        if (StringUtils.isEmpty(beanName)) {
            throw new IllegalArgumentException("invalid driver type");
        }
        return beanName;
    }
}
