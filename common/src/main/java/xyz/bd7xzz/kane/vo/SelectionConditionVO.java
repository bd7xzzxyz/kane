package xyz.bd7xzz.kane.vo;

import lombok.Data;

import java.util.List;

/**
 * @author baodi1
 * @description: 筛选条件
 * @date 2021/8/16 11:13 上午
 */
@Data
public class SelectionConditionVO {
    private short relation;
    private long labelId;
    private String fullName;
    private List<Object> values;
    private List<String> operators;
    private List<SelectionConditionVO> conditions;
}
