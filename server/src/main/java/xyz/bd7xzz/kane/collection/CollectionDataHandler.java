package xyz.bd7xzz.kane.collection;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.CollectionRequestMethodConstraint;
import xyz.bd7xzz.kane.constraint.EngineTypeConstraint;
import xyz.bd7xzz.kane.constraint.ServiceHandler;
import xyz.bd7xzz.kane.engine.DML;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.CollectionVO;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.TableVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集数据处理器
 * @date 7/4/21 5:53 PM
 */
@Slf4j
public abstract class CollectionDataHandler {
    @Autowired
    private LocalCache localCache;

    /**
     * 提取json数据
     *
     * @param collectionVO 采集数据VO
     */
    public abstract void extract(CollectionVO collectionVO, ServiceHandler serviceHandler);

    /**
     * 抽取并保存数据
     *
     * @param collectionVO 采集数据
     */
    public void extractAndSave(CollectionVO collectionVO) {
        DataSourceConfigVO dataSourceConfigVO = getDataSourceConfig(collectionVO.getDataSourceId());
        if (null == dataSourceConfigVO) {
            log.error("cannot found data source config when extract and save collection result");
            return;
        }
        extract(collectionVO, new ServiceHandler() {
            @Override
            public <P> void doService(P param) {
                try {
                    TableVO table = (TableVO) param;
                    String beanName = EngineTypeConstraint.getDMLBeanName(dataSourceConfigVO.getType());
                    if (CollectionRequestMethodConstraint.INSERT.equals(table.getMethod())) {
                        SpringContextUtil.getBean(beanName, DML.class).insert(table.getTable());
                    } else if (CollectionRequestMethodConstraint.DELETE.equals(table.getMethod())) {
                        SpringContextUtil.getBean(beanName, DML.class).delete(table.getTable());
                    } else if (CollectionRequestMethodConstraint.UPDATE.equals(table.getMethod())) {
                        SpringContextUtil.getBean(beanName, DML.class).update(table.getTable());
                    } else {
                        log.error("extract and save to engine error,invalid request method={}", table.getMethod());
                    }
                } catch (Exception e) {
                    log.error("extract and save to engine error", e);
                }
            }
        });
    }

    /**
     * 从缓存中获取采集字段
     *
     * @param dataSourceId 数据源id
     * @return 采集字段vo
     */
    protected List<CollectionFieldVO> getCollectionFields(long dataSourceId) {
        try {
            return localCache.getCollectionFieldCache().get(dataSourceId);
        } catch (ExecutionException e) {
            log.error("get collection fields error when extract collection data", e);
            return Lists.newArrayListWithCapacity(0);
        }
    }

    /**
     * 获取业务主键源字段名
     *
     * @param collectionFields 采集字段
     * @return 字段名
     */
    protected String getPrimaryKeySourceFieldName(List<CollectionFieldVO> collectionFields) {
        for (CollectionFieldVO collectionField : collectionFields) {
            if (collectionField.isPrimaryKey()) {
                return collectionField.getSourceField();
            }
        }
        throw new IllegalArgumentException("cannot found primary key");
    }

    /**
     * 获取数据源配置
     *
     * @param dataSourceId 数据源id
     * @return 数据源配置vo
     */
    private DataSourceConfigVO getDataSourceConfig(long dataSourceId) {
        try {
            return localCache.getDataSourceConfigCache().get(dataSourceId);
        } catch (ExecutionException e) {
            log.error("get data source config error when extract collection data", e);
            return null;
        }
    }
}
