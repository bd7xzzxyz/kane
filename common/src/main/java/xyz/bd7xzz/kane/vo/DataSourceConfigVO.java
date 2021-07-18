package xyz.bd7xzz.kane.vo;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源配置
 * @date 7/4/21 12:15 PM
 */
@Data
public class DataSourceConfigVO {
    private long id;
    private int type;
    private String name;
    private String driver;
    private int engine;
    private String cron;
    private String version;
}
