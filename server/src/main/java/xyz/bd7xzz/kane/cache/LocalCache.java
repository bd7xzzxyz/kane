package xyz.bd7xzz.kane.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.configmanager.CollectionFieldManager;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.SelectionTaskVO;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 本地缓存
 * @date 7/4/21 2:00 PM
 */
@Component
@Slf4j
public class LocalCache {

    private final DataSourceConfigManager dataSourceConfigManager;
    private final CollectionFieldManager collectionFieldManager;


    private final LoadingCache<Long, ConnectionVO> connectionCache =
            CacheBuilder.newBuilder()
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, ConnectionVO>() {
                        @Override
                        public ListenableFuture<ConnectionVO> reload(Long key, ConnectionVO oldValue) throws Exception {
                            return ListenableFutureTask.create(() -> loadConnectionById(key, oldValue));
                        }

                        @Override
                        public ConnectionVO load(Long id) throws Exception {
                            return loadConnectionById(id, null);
                        }
                    });

    private final LoadingCache<Long, DataSourceConfigVO> dataSourceConfigCache =
            CacheBuilder.newBuilder()
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, DataSourceConfigVO>() {
                        @Override
                        public ListenableFuture<DataSourceConfigVO> reload(Long key, DataSourceConfigVO oldValue) throws Exception {
                            return ListenableFutureTask.create(() -> loadDataSourceConfigById(key));
                        }

                        @Override
                        public DataSourceConfigVO load(Long id) throws Exception {
                            return loadDataSourceConfigById(id);
                        }
                    });

    private final LoadingCache<Long, List<CollectionFieldVO>> collectionFieldCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, List<CollectionFieldVO>>() {
                @Override
                public ListenableFuture<List<CollectionFieldVO>> reload(Long key, List<CollectionFieldVO> oldValue) throws Exception {
                    return ListenableFutureTask.create(() -> loadCollectionField(key));
                }

                @Override
                public List<CollectionFieldVO> load(Long id) throws Exception {
                    return loadCollectionField(id);
                }
            });

    private final LoadingCache<Long, SelectionTaskVO> selectionTaskCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, SelectionTaskVO>() {
        @Override
        public SelectionTaskVO load(Long key) throws Exception {
            //TODO
            return null;
        }
    });

    @Autowired
    public LocalCache(DataSourceConfigManager dataSourceConfigManager, CollectionFieldManager collectionFieldManager) {
        this.dataSourceConfigManager = dataSourceConfigManager;
        this.collectionFieldManager = collectionFieldManager;
    }

    @PostConstruct
    public void init() {
        //TODO
    }

    /**
     * 获取链接缓存
     *
     * @return 缓存对象
     */
    public LoadingCache<Long, ConnectionVO> getConnectionCache() {
        return connectionCache;
    }

    /**
     * 获取采集字段缓存
     *
     * @return 采集字段缓存对象
     */
    public LoadingCache<Long, List<CollectionFieldVO>> getCollectionFieldCache() {
        return collectionFieldCache;
    }

    /**
     * 获取数据源缓存
     *
     * @return 数据源缓存对象
     */
    public LoadingCache<Long, DataSourceConfigVO> getDataSourceConfigCache() {
        return dataSourceConfigCache;
    }

    /**
     * 获取筛选任务缓存
     *
     * @return 筛选任务缓存对象
     */
    public LoadingCache<Long, SelectionTaskVO> getSelectionTaskCache() {
        return selectionTaskCache;
    }

    /**
     * 按id加载链接到缓存
     *
     * @param id       数据源id
     * @param oldValue 缓存中的旧链接
     * @return 链接缓存对象
     */
    private ConnectionVO loadConnectionById(long id, ConnectionVO oldValue) {
        DataSourceConfigVO dataSourceConfig = dataSourceConfigManager.getDataSourceById(id);
        if (null == dataSourceConfig) {
            return null;
        }

        if (null != oldValue && oldValue.getVersion().equals(dataSourceConfig.getVersion())) {
            return oldValue;
        }
        Class<? extends BasicDriverVO> driverVOClass = DataSourceDriverConstraint.getVOClass(dataSourceConfig.getType());
        BasicDriverVO driverVO = JSONUtil.parseObject(dataSourceConfig.getDriver(), driverVOClass);
        return ConnectionHandler.createConnectionWithOutCache(dataSourceConfig.getType(), driverVO);
    }

    /**
     * 加载采集字段
     *
     * @param dataSourceId 采集数据源id
     * @return 采集字段vo对象
     */
    private List<CollectionFieldVO> loadCollectionField(long dataSourceId) {
        return collectionFieldManager.getCollectionFieldByDataSourceId(dataSourceId);
    }

    /**
     * 加载数据源配置
     *
     * @param dataSourceId 数据源id
     * @return 数据源配置
     */
    private DataSourceConfigVO loadDataSourceConfigById(long dataSourceId) {
        return dataSourceConfigManager.getDataSourceById(dataSourceId);
    }

}
