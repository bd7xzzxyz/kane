package xyz.bd7xzz.kane.vo;

import lombok.Data;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置
 * @date 7/25/21 10:45 PM
 */
@Data
public class SelectionConfigVO {
    private long id;
    private String name;
    private String outputColumns;
    private String cron;
    private ConnectionVO connection;
    private short relation;
    private List<SelectionConditionVO> conditions;
    private String version;
}
