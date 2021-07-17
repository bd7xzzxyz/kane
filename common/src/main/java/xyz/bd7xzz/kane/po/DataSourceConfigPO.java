package xyz.bd7xzz.kane.po;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 处理新对象字段空值
     *
     * @param oldConfig 旧对象
     * @return 新对象
     */
    public DataSourceConfigPO diffAndSet(DataSourceConfigPO oldConfig) {
        if (id <= 0) {
            id = oldConfig.getId();
        }
        if (type <= 0) {
            type = oldConfig.getType();
        }
        if (StringUtils.isEmpty(name)) {
            name = oldConfig.getName();
        }
        if (StringUtils.isEmpty(driver)) {
            driver = oldConfig.getDriver();
        }
        if (engine <= 0) {
            engine = oldConfig.getEngine();
        }
        if (StringUtils.isEmpty(cron)) {
            cron = oldConfig.getCron();
        }
        return this;
    }
}
