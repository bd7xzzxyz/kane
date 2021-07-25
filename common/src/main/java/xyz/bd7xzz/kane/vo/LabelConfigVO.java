package xyz.bd7xzz.kane.vo;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置
 * @date 7/18/21 3:50 PM
 */
@Data
public class LabelConfigVO {
    private long id;
    private String name;
    private long fieldId;
    private String comment;
    private String dimension;

}
