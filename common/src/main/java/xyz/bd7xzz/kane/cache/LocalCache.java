package xyz.bd7xzz.kane.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.vo.ConnectionVO;

import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 本地缓存
 * @date 7/4/21 2:00 PM
 */
@Component
public class LocalCache {

    private final LoadingCache<Long, ConnectionVO> connectionCache =
            CacheBuilder.newBuilder()
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, ConnectionVO>() {
                        @Override
                        public ListenableFuture<ConnectionVO> reload(Long key, ConnectionVO oldValue) throws Exception {
                            //TODO
                            return null;
                        }

                        @Override
                        public ConnectionVO load(Long id) throws Exception {
                            //TODO load
                            return null;
                        }
                    });

    /**
     * 获取链接缓存
     *
     * @return 缓存对象
     */
    public LoadingCache<Long, ConnectionVO> getConnectionCache() {
        return connectionCache;
    }
}
