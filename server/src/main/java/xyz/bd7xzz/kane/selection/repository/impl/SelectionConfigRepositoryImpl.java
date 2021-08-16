package xyz.bd7xzz.kane.selection.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.po.SelectionConfigPO;
import xyz.bd7xzz.kane.selection.repository.SelectionConfigRepository;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选配置落地
 * @date 7/25/21 10:47 PM
 */
@Repository
public class SelectionConfigRepositoryImpl implements SelectionConfigRepository {

    private static final String BASE_COLUMNS = "`c_id` AS `id`,`name`,`output_columns`,`cron`,`connection`,`relation`,`conditions`,`version`";
    private static final String GET_SQL = "SELECT " + BASE_COLUMNS + " FROM `t_selection_config` WHERE `c_id` = ? AND `is_delete` = 0 LIMIT 1";
    private static final String SAVE_SQL = "INSERT INTO `t_selection_config`(`c_id`,`name`,`output_columns`,`cron`,`connection`,`relation`" +
            ",`conditions`,`version`,`is_delete`,`ctime`,`utime`)VALUES(?,?,?,?,?,?,?,?,0,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private static final String DELETE_SQL = "UPDATE `t_selection_config` SET `is_delete` = 1 AND `utime` = CURRENT_TIMESTAMP() WHERE `c_id` = ?";
    private static final String UPDATE_SQL = "UPDATE `t_selection_config` SET `name` = ?,`output_columns` = ?,`cron` = ?,`connection` = ?,`relation` = ?,`conditions` = ?,`version` = ?,`utime` = CURRENT_TIMESTAMP() WHERE `c_id` = ?";

    private final JdbcTemplate jdbcTemplate;

    public SelectionConfigRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SelectionConfigPO get(long id) {
        return jdbcTemplate.queryForObject(GET_SQL, SelectionConfigPO.class, id);
    }

    @Override
    public void save(SelectionConfigPO selectionConfigPO) {
        jdbcTemplate.update(SAVE_SQL,
                selectionConfigPO.getId(),
                selectionConfigPO.getName(),
                selectionConfigPO.getOutputColumns(),
                selectionConfigPO.getCron(),
                selectionConfigPO.getConnection(),
                selectionConfigPO.getRelation(),
                selectionConfigPO.getConditions(),
                selectionConfigPO.getVersion());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public void update(SelectionConfigPO configPO) {
        jdbcTemplate.update(UPDATE_SQL,
                configPO.getName(),
                configPO.getOutputColumns(),
                configPO.getCron(),
                configPO.getConnection(),
                configPO.getRelation(),
                configPO.getConditions(),
                configPO.getVersion());
    }
}
