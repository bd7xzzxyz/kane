package xyz.bd7xzz.kane.configmanager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.configmanager.repository.CollectionFieldRepository;
import xyz.bd7xzz.kane.po.CollectionFieldPO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段落地
 * @date 7/14/21 9:59 PM
 */
@Repository
public class CollectionFieldRepositoryImpl implements CollectionFieldRepository {

    private static final String BASE_COLUMN = "`f_id` AS `id`,`ds_id` AS `data_source_id`,`name`,`source_field`,`target_field`," +
            "`type`,`comment`,`version`";
    private static final String SAVE_SQL = "INSERT INTO `t_collection_field`(`f_id`,`ds_id`,`name`,`source_field`,`target_field`,`type`,`comment`,`version`,`is_delete`,`ctime`,`utime`)VALUES(?,?,?,?,?,?,?,?,0,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private static final String GET_BY_ID_SQL = "SELECT " + BASE_COLUMN + " FROM `t_collection_field` WHERE `is_delete` = 0 AND `f_id` = ? LIMIT 1";
    private static final String DELETE_COLLECTION_FIELD_SQL = "UPDATE `t_collection_field` SET `is_delete` = 1,`utime` = CURRENT_TIMESTAMP() WHERE `f_id` = ?";
    private static final String GET_COLLECTION_FIELD_BY_DATA_SOURCE_ID_SQL = "SELECT " + BASE_COLUMN + " FROM `t_collection_field` WHERE `is_delete` = 0  AND `ds_id` = ?";
    private static final String UPDATE_COLLECTION_FIELD_SQL = "UPDATE `t_collection_field` SET `ds_id` = ?,`name` = ?,`source_field` = ?,`target_field` = ?,`type` = ?,`comment` = ?,`version` = ?,`utime` = CURRENT_TIMESTAMP() WHERE `f_id` = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CollectionFieldRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void batchSave(List<CollectionFieldPO> fieldPOS) {
        jdbcTemplate.batchUpdate(SAVE_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, fieldPOS.get(i).getId());
                ps.setLong(2, fieldPOS.get(i).getDataSourceId());
                ps.setString(3, fieldPOS.get(i).getName());
                ps.setString(4, fieldPOS.get(i).getSourceField());
                ps.setString(5, fieldPOS.get(i).getTargetField());
                ps.setInt(6, fieldPOS.get(i).getType());
                ps.setString(7, fieldPOS.get(i).getComment());
                ps.setString(8, fieldPOS.get(i).getVersion());
            }

            @Override
            public int getBatchSize() {
                return fieldPOS.size();
            }
        });
    }

    @Override
    public CollectionFieldPO getById(long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_SQL, CollectionFieldPO.class, id);
    }

    @Override
    public void deleteCollectionField(long id) {
        jdbcTemplate.update(DELETE_COLLECTION_FIELD_SQL, id);
    }

    @Override
    public List<CollectionFieldPO> getCollectionFieldByDataSourceId(long dataSourceId) {
        return jdbcTemplate.query(GET_COLLECTION_FIELD_BY_DATA_SOURCE_ID_SQL, new BeanPropertyRowMapper<>(CollectionFieldPO.class), dataSourceId);
    }

    @Override
    public void updateCollectionField(CollectionFieldPO collectionFieldPO) {
        jdbcTemplate.update(UPDATE_COLLECTION_FIELD_SQL,
                collectionFieldPO.getDataSourceId(),
                collectionFieldPO.getName(),
                collectionFieldPO.getSourceField(),
                collectionFieldPO.getTargetField(),
                collectionFieldPO.getType(),
                collectionFieldPO.getComment(),
                collectionFieldPO.getVersion());
    }
}
