package xyz.bd7xzz.kane.configmanager.repository.impl;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.configmanager.repository.DataSourceConfigRepository;
import xyz.bd7xzz.kane.po.DataSourceConfigPO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 数据源配置落地
 * @date 7/4/21 11:57 AM
 */
@Repository
public class DataSourceConfigRepositoryImpl implements DataSourceConfigRepository {

    private static final String BASE_COLUMN = "`ds_id` AS `id`,`name`,`type`,`engine`,`driver`,`version`,`cron`";
    private static final String SAVE_SQL = "INSERT INTO `t_datasource_config`(`ds_id`,`name`,`type`,`engine`,`driver`,`version`,`cron`,`is_delete`,`ctime`,`utime`)VALUES" +
            "(?,?,?,?,?,?,0,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private static final String GET_SQL = "SELECT " + BASE_COLUMN + " FROM `t_datasource_config` WHERE `ds_id` = ? AND `is_delete` = 0 LIMIT 1";
    private static final String DELETE_SQL = "UPDATE `t_datasource_config` SET `is_delete` = 1,`utime` = CURRENT_TIMESTAMP() WHERE `ds_id` = ?";
    private static final String UPDATE_SQL = "UPDATE `t_datasource_config` SET `name` = ?,`type` = ?,`engine` = ?,`driver` = ?,`version` = ?,`cron` = ?,`utime` = CURRENT_TIMESTAMP() WHERE `ds_id` = ?";
    private static final String LIST_SQL = "SELECT " + BASE_COLUMN + " FROM `t_datasource_config` WHERE `is_delete` = 0 ORDER BY `id` DESC";
    private static final String BATCH_GET_DATA_SOURCE_BY_ID_SQL = "SELECT " + BASE_COLUMN + " FROM `t_datasource_config` WHERE `is_delete` = 0 AND `ds_id` IN (:ids) ORDER BY `id` DESC";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataSourceConfigRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(DataSourceConfigPO configPO) {
        jdbcTemplate.update(SAVE_SQL,
                configPO.getId(),
                configPO.getName(),
                configPO.getType(),
                configPO.getEngine(),
                configPO.getDriver(),
                configPO.getVersion(),
                configPO.getCron());
    }

    @Override
    public DataSourceConfigPO get(long id) {
        return jdbcTemplate.queryForObject(GET_SQL, DataSourceConfigPO.class, id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public void update(DataSourceConfigPO configPO) {
        jdbcTemplate.update(UPDATE_SQL,
                configPO.getName(),
                configPO.getType(),
                configPO.getEngine(),
                configPO.getDriver(),
                configPO.getVersion(),
                configPO.getCron(),
                configPO.getId()
        );
    }

    @Override
    public List<DataSourceConfigPO> listDataSource() {
        return jdbcTemplate.query(LIST_SQL, new BeanPropertyRowMapper<>(DataSourceConfigPO.class));
    }

    @Override
    public List<DataSourceConfigPO> batchGetDataSourceById(List<Long> ids) {
        return new NamedParameterJdbcTemplate(jdbcTemplate).
                query(BATCH_GET_DATA_SOURCE_BY_ID_SQL, ImmutableMap.of("ids", ids), new BeanPropertyRowMapper<>(DataSourceConfigPO.class));
    }

}
