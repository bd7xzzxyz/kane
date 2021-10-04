package xyz.bd7xzz.kane.selection.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.po.SelectionTaskPO;
import xyz.bd7xzz.kane.selection.repository.SelectionTaskRepository;

/**
 * @author baodi1
 * @description: 筛选任务落地
 * @date 2021/8/24 4:07 下午
 */
@Repository
public class SelectionTaskRepositoryImpl implements SelectionTaskRepository {

    private static final String SAVE_SQL = "INSERT INTO `t_selection_task`(`tid`,`config_id`,`config`,`is_delete`,`ctime`,`utime`)VALUES(?,?,?,0,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private static final String GET_SQL = "SELECT `config_id`,`config`,`ctime`,`utime` FROM `t_selection_task` WHERE `tid` = ? AND `is_delete` = 0 LIMIT 1";

    private final JdbcTemplate jdbcTemplate;

    public SelectionTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SelectionTaskPO taskPO) {
        jdbcTemplate.update(SAVE_SQL, taskPO.getId(), taskPO.getConfigId());
    }

    @Override
    public SelectionTaskPO get(long taskId) {
        return jdbcTemplate.queryForObject(GET_SQL, new BeanPropertyRowMapper<>(SelectionTaskPO.class), taskId);
    }
}
