package xyz.bd7xzz.kane.selection.repository.impl;

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
    @Override
    public SelectionConfigPO getSelectionPO(long id) {
        return null;
    }

    @Override
    public void save(SelectionConfigPO selectionConfigPO) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void update(SelectionConfigPO configPO) {

    }
}
