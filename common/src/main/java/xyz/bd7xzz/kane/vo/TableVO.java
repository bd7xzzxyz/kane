package xyz.bd7xzz.kane.vo;

import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Getter;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 表
 * @date 7/18/21 12:01 PM
 */
@Builder
@Getter
public class TableVO {
    private short method;
    private Table<Long, String, Object> table;
}
