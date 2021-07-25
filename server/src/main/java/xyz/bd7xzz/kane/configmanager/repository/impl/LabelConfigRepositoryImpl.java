package xyz.bd7xzz.kane.configmanager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.bd7xzz.kane.configmanager.repository.LabelConfigRepository;
import xyz.bd7xzz.kane.po.LabelConfigPO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 标签配置落地
 * @date 7/18/21 3:48 PM
 */
@Repository
public class LabelConfigRepositoryImpl implements LabelConfigRepository {

    private static final String BASE_COLUMN = "`lid` AS `id`,`name`,`field_id`,`comment`,`dimension`";
    private static final String SAVE_SQL = "INSERT INTO `t_label_config`(`lid`,`name`,`field_id`,`comment`,`dimension`,`is_delete`,`ctime`,`utime`)VALUES(?,?,?,?,?,0,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
    private static final String GET_BY_ID_SQL = "SELECT " + BASE_COLUMN + " FROM t_label_config WHERE `lid` = ? AND `is_delete` = 0 LIMIT 1";
    private static final String DELETE_LABEL_CONFIG_SQL = "UPDATE `t_label_config` SET `is_delete` = 1,`utime` = CURRENT_TIMESTAMP() WHERE `lid` = ?";
    private static final String UPDATE_LABEL_CONFIG_SQL = "UPDATE `t_label_config` SET `name` = ?,`field_id` = ?,`comment` = ?,`dimension` = ?,`utime` = CURRENT_TIMESTAMP() WHERE `lid` = ?";
    private static final String LIST_LABEL_CONFIG_SQL = "SELECT " + BASE_COLUMN + " FROM WHERE `is_delete` = 0 ORDER by `id` DESC";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LabelConfigRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(LabelConfigPO labelConfigPO) {
        jdbcTemplate.update(SAVE_SQL,
                labelConfigPO.getId(),
                labelConfigPO.getName(),
                labelConfigPO.getFieldId(),
                labelConfigPO.getComment(),
                labelConfigPO.getDimension());
    }

    @Override
    public LabelConfigPO getById(long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_SQL, LabelConfigPO.class, id);
    }

    @Override
    public void deleteLabelConfig(long id) {
        jdbcTemplate.update(DELETE_LABEL_CONFIG_SQL, id);
    }

    @Override
    public void updateLabelConfig(LabelConfigPO labelConfigPO) {
        jdbcTemplate.update(UPDATE_LABEL_CONFIG_SQL, labelConfigPO.getName(), labelConfigPO.getFieldId(), labelConfigPO.getComment(), labelConfigPO.getDimension(), labelConfigPO.getId());
    }

    @Override
    public List<LabelConfigPO> listLabelConfig() {
        return jdbcTemplate.query(LIST_LABEL_CONFIG_SQL, new BeanPropertyRowMapper<>(LabelConfigPO.class));
    }
}
