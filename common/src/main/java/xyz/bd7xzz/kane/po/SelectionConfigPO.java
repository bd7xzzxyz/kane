package xyz.bd7xzz.kane.po;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置
 * @date 7/25/21 10:45 PM
 */
@Data
public class SelectionConfigPO {

    private long id;
    private String name;
    private String outputColumns;
    private String cron;
    private String connection;
    private short relation;
    private String conditions;
    private String version;

    /**
     * 处理新对象字段空值
     *
     * @param old 旧对象
     * @return 新对象
     */
    public SelectionConfigPO diffAndSet(SelectionConfigPO old) {
        if (id <= 0) {
            id = old.getId();
        }
        if (StringUtils.isEmpty(name)) {
            name = old.getName();
        }
        if (StringUtils.isEmpty(outputColumns)) {
            name = old.getOutputColumns();
        }
        if (StringUtils.isEmpty(cron)) {
            cron = old.getCron();
        }
        if (StringUtils.isEmpty(connection)) {
            connection = old.getConnection();
        }
        if (relation <= 0) {
            relation = old.getRelation();
        }
        if (StringUtils.isEmpty(conditions)) {
            conditions = old.getConditions();
        }
        return this;
    }
}
