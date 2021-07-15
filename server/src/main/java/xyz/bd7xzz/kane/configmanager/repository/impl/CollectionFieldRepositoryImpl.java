package xyz.bd7xzz.kane.configmanager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.configmanager.repository.CollectionFieldRepository;
import xyz.bd7xzz.kane.po.CollectionFieldPO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段落地
 * @date 7/14/21 9:59 PM
 */
@Repository
public class CollectionFieldRepositoryImpl implements CollectionFieldRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CollectionFieldRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void batchSave(List<CollectionFieldPO> fieldPOS) {
        //TODO
    }

    @Override
    public CollectionFieldPO getById(long id) {
        //TODO
        return null;
    }
}
