package xyz.bd7xzz.kane.vo.driver;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源驱动
 * @date 7/4/21 3:11 PM
 */
@Data
public class BasicDriverVO {
    private long id;
    private int serializeType;
    private String version;
    private int type;
}
