package xyz.bd7xzz.kane.selection.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.constraint.SelectionConstraints;
import xyz.bd7xzz.kane.po.SelectionTaskHistoryPO;
import xyz.bd7xzz.kane.selection.repository.SelectionTaskHistoryRepository;

/**
 * @author baodi1
 * @description: 筛选任务历史落地
 * @date 2021/10/4 2:37 下午
 */
@Repository
public class SelectionTaskHistoryRepositoryImpl implements SelectionTaskHistoryRepository {
    private static final String UPDATE_ERROR_STATUS_SQL = "UPDATE `t_selection_task_history` SET `status` = ?,`error_message` = ?,`code`= ? " +
            "WHERE `task_id` = ? ORDER BY `execute_time` DESC LIMIT 1";
    private static final String UPDATE_STATUS_SQL = "UPDATE `t_selection_task_history` SET `status` = ?,`success_message` = ?,`code`= ? " +
            "WHERE `task_id` = ? ORDER BY `execute_time` DESC LIMIT 1";
    private static final String SAVE_SQL = "INSERT INTO `t_selection_task_history`(`execute_time`,`task_id`,`status`,`error_message`,`success_message`,`code`)" +
            "VALUES(?,?,?,?,?,?)";
    private static final String GET_LATEST_HISTORY = "SELECT `id`,`execute_time`,`task_id`,`status`,`error_message`,`success_message`,`code` " +
            "FROM `t_selection_task_history` WHERE `task_id` = ? ORDER BY `execute_time` DESC LIMIT 1";
    private final JdbcTemplate jdbcTemplate;

    public SelectionTaskHistoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void updateStatus(long taskId, short taskStatus, String message, int code) {
        if (taskStatus == SelectionConstraints.TASK_STATE_ERROR) {
            jdbcTemplate.update(UPDATE_ERROR_STATUS_SQL, taskStatus, message, code, taskId);
        } else {
            jdbcTemplate.update(UPDATE_STATUS_SQL, taskStatus, message, code, taskId);
        }
    }

    @Override
    public void save(SelectionTaskHistoryPO taskHistoryPO) {
        jdbcTemplate.update(SAVE_SQL, taskHistoryPO.getExecuteTime(), taskHistoryPO.getTaskId(),
                taskHistoryPO.getStatus(), taskHistoryPO.getErrorMessage(), taskHistoryPO.getSuccessMessage(), taskHistoryPO.getCode());
    }

    @Override
    public SelectionTaskHistoryPO getLatestHistory(long taskId) {
        return jdbcTemplate.queryForObject(GET_LATEST_HISTORY, new BeanPropertyRowMapper<>(SelectionTaskHistoryPO.class), taskId);
    }
}
