package xyz.bd7xzz.kane.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 雪花算法配置
 * @date 7/4/21 11:21 AM
 */
@Component
@ConfigurationProperties("xyz.bd7xzz.kane.util.snowflake")
@Data
public class SnowFlakeProperties {
    private int dataCenterId;
    private int machineId;
}
