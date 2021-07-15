package xyz.bd7xzz.kane.configmanager.repository;

import xyz.bd7xzz.kane.po.CollectionFieldPO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段落地
 * @date 7/14/21 9:58 PM
 */
public interface CollectionFieldRepository {

    /**
     * 批量保存
     *
     * @param fieldPOS 采集字段
     */
    void batchSave(List<CollectionFieldPO> fieldPOS);

    /**
     * 根据id获取采集字段
     *
     * @param id 采集字段id
     * @return 采集字段
     */
    CollectionFieldPO getById(long id);
}
