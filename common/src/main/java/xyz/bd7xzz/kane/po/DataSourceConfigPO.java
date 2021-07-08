package xyz.bd7xzz.kane.po;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源配置
 * @date 7/4/21 8:14 PM
 */
@Data
public class DataSourceConfigPO {
    private long id;
    private int type;
    private String name;
    private String driver;
    private int engine;
    private String version;
    private String cron;
}
