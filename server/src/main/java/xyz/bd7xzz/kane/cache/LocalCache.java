package xyz.bd7xzz.kane.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.configmanager.CollectionFieldManager;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigManager;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.selection.SelectionConfigManager;
import xyz.bd7xzz.kane.selection.SelectionTaskManager;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.*;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
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
    private final SelectionConfigManager selectionConfigManager;
    private final SelectionTaskManager selectionTaskManager;

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
                            return ListenableFutureTask.create(() -> loadDataSourceConfigById(key, oldValue));
                        }

                        @Override
                        public DataSourceConfigVO load(Long id) throws Exception {
                            return loadDataSourceConfigById(id, null);
                        }
                    });

    private final LoadingCache<Long, List<CollectionFieldVO>> collectionFieldCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, List<CollectionFieldVO>>() {
                @Override
                public ListenableFuture<List<CollectionFieldVO>> reload(Long key, List<CollectionFieldVO> oldValue) throws Exception {
                    return ListenableFutureTask.create(() -> loadCollectionField(key, oldValue));
                }

                @Override
                public List<CollectionFieldVO> load(Long id) throws Exception {
                    return loadCollectionField(id, null);
                }
            });

    private final LoadingCache<Long, SelectionTaskVO> selectionTaskCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, SelectionTaskVO>() {
        @Override
        public SelectionTaskVO load(Long key) throws Exception {
            return loadSelectionTaskVOById(key);
        }
    });

    private final LoadingCache<Long, SelectionConfigVO> selectionConfigCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, SelectionConfigVO>() {
                @Override
                public ListenableFuture<SelectionConfigVO> reload(Long key, SelectionConfigVO oldValue) throws Exception {
                    return ListenableFutureTask.create(() -> loadSelectionConfigById(key, oldValue));
                }

                @Override
                public SelectionConfigVO load(Long key) throws Exception {
                    return loadSelectionConfigById(key, null);
                }
            });

    @Autowired
    public LocalCache(DataSourceConfigManager dataSourceConfigManager, CollectionFieldManager collectionFieldManager,
                      SelectionConfigManager selectionConfigManager, SelectionTaskManager selectionTaskManager) {
        this.dataSourceConfigManager = dataSourceConfigManager;
        this.collectionFieldManager = collectionFieldManager;
        this.selectionConfigManager = selectionConfigManager;
        this.selectionTaskManager = selectionTaskManager;
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
     * 获取筛选配置缓存
     *
     * @return 筛选配置缓存对象
     */
    public LoadingCache<Long, SelectionConfigVO> getSelectionConfigCache() {
        return selectionConfigCache;
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
     * @param oldValues    缓存中的旧数据
     * @return 采集字段vo对象
     */
    private List<CollectionFieldVO> loadCollectionField(long dataSourceId, List<CollectionFieldVO> oldValues) {
        List<CollectionFieldVO> collectionFieldVOS = collectionFieldManager.getCollectionFieldByDataSourceId(dataSourceId);
        if (CollectionUtils.isEmpty(collectionFieldVOS)) {
            return null;
        }
        if (null != oldValues && oldValues.size() != collectionFieldVOS.size()) {
            return collectionFieldVOS;
        }
        Map<Long, String> diffMapper = Maps.newHashMap();
        oldValues.forEach(collectionFieldVO -> diffMapper.put(collectionFieldVO.getId(), collectionFieldVO.getVersion()));
        if (null != oldValues) {
            for (int i = 0; i < collectionFieldVOS.size(); i++) {
                if (!diffMapper.containsKey(collectionFieldVOS.get(i))) {
                    return collectionFieldVOS;
                }
                if (!diffMapper.get(collectionFieldVOS.get(i)).equals(oldValues.get(i).getVersion())) {
                    return collectionFieldVOS;
                }
            }
        }
        return oldValues;
    }

    /**
     * 加载数据源配置
     *
     * @param dataSourceId 数据源id
     * @param oldValue     缓存中的旧数据
     * @return 数据源配置
     */
    private DataSourceConfigVO loadDataSourceConfigById(long dataSourceId, DataSourceConfigVO oldValue) {
        DataSourceConfigVO dataSourceConfigVO = dataSourceConfigManager.getDataSourceById(dataSourceId);
        if (null == dataSourceConfigVO) {
            return null;
        }
        if (null != oldValue && oldValue.getVersion().equals(dataSourceConfigVO.getVersion())) {
            return oldValue;
        }
        return dataSourceConfigVO;
    }

    /**
     * 根据筛选配置id加载筛选配置
     *
     * @param configId 配置id
     * @param oldValue 缓存中的旧数据
     * @return 筛选配置
     */
    private SelectionConfigVO loadSelectionConfigById(long configId, SelectionConfigVO oldValue) {
        SelectionConfigVO selectionConfigVO = selectionConfigManager.getSelection(configId);
        if (null == selectionConfigVO) {
            return null;
        }
        if (null != oldValue && oldValue.getVersion().equals(selectionConfigVO.getVersion())) {
            return oldValue;
        }
        return selectionConfigVO;
    }

    /**
     * 按任务id加载筛选任务
     *
     * @param taskId 筛选任务id
     * @return 筛选任务
     */
    private SelectionTaskVO loadSelectionTaskVOById(long taskId) {
        return selectionTaskManager.getTask(taskId);
    }
}
