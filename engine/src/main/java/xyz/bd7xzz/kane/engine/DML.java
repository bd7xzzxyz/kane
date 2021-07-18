package xyz.bd7xzz.kane.engine;

import com.google.common.collect.Table;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 增删改接口
 * @date 7/18/21 10:03 AM
 */
public interface DML {
    /**
     * 插入数据
     *
     * @param table 表
     */
    void insert(Table table);

    /**
     * 按业务主键删除
     *
     * @param table 表
     */
    void delete(Table table);

    /**
     * 修改数据
     *
     * @param table 表
     */
    void update(Table table);
}
