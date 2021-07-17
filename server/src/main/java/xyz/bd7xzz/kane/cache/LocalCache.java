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
import xyz.bd7xzz.kane.configmanager.repository.DataSourceConfigRepository;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.po.DataSourceConfigPO;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.ConnectionVO;
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

    private final DataSourceConfigRepository dataSourceConfigRepository;
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

    @Autowired
    public LocalCache(DataSourceConfigRepository dataSourceConfigRepository, CollectionFieldManager collectionFieldManager) {
        this.dataSourceConfigRepository = dataSourceConfigRepository;
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
     * @return 采集字段对象
     */
    public LoadingCache<Long, List<CollectionFieldVO>> getCollectionFieldCache() {
        return collectionFieldCache;
    }

    /**
     * 按id加载链接到缓存
     *
     * @param id       数据源id
     * @param oldValue 缓存中的旧链接
     * @return 链接对象
     */
    private ConnectionVO loadConnectionById(long id, ConnectionVO oldValue) {
        DataSourceConfigPO dataSourceConfigPO = dataSourceConfigRepository.get(id);
        if (null == dataSourceConfigPO) {
            return null;
        }

        if (null != oldValue && oldValue.getVersion().equals(dataSourceConfigPO.getVersion())) {
            return oldValue;
        }
        Class<? extends BasicDriverVO> driverVOClass = DataSourceDriverConstraint.getVOClass(dataSourceConfigPO.getType());
        BasicDriverVO driverVO = JSONUtil.parseObject(dataSourceConfigPO.getDriver(), driverVOClass);
        return ConnectionHandler.createConnectionWithOutCache(dataSourceConfigPO.getType(), driverVO);
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
}
