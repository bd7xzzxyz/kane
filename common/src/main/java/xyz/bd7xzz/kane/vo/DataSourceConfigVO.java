package xyz.bd7xzz.kane.vo;

import lombok.Data;

@Data
public class DataSourceConfigVO {
    private int type;
    private String name;
    private String driver;
    private int engine;
}
