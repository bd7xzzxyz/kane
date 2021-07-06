package xyz.bd7xzz.kane.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.configmanager.repository.DataSourceConfigRepository;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.po.DataSourceConfigPO;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 本地缓存
 * @date 7/4/21 2:00 PM
 */
@Component
public class LocalCache {

    private final DataSourceConfigRepository dataSourceConfigRepository;

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

    @Autowired
    public LocalCache(DataSourceConfigRepository dataSourceConfigRepository) {
        this.dataSourceConfigRepository = dataSourceConfigRepository;
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
}
