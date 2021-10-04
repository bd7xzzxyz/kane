package xyz.bd7xzz.kane.po;

import lombok.Data;

/**
 * @author baodi1
 * @description: 筛选任务
 * @date 2021/8/24 4:08 下午
 */
@Data
public class SelectionTaskPO {
    private long id;
    private long configId;
    private String config;
    private long ctime;
    private long utime;
}
