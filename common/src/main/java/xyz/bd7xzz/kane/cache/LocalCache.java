package xyz.bd7xzz.kane.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 本地缓存
 * @date 7/4/21 2:00 PM
 */
@Component
public class LocalCache {

    private final LoadingCache<Long, ConcurrentMessageListenerContainer> kafkaListenerCache =
            CacheBuilder.newBuilder()
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, ConcurrentMessageListenerContainer>() {
                        @Override
                        public ListenableFuture<ConcurrentMessageListenerContainer> reload(Long key, ConcurrentMessageListenerContainer oldValue) throws Exception {
                            ListenableFutureTask<ConcurrentMessageListenerContainer> task = ListenableFutureTask.create(new Callable<ConcurrentMessageListenerContainer>() {
                                @Override
                                public ConcurrentMessageListenerContainer call() throws Exception {
                                    //TODO load
                                    return null;
                                }
                            });
                            return task;
                        }

                        @Override
                        public ConcurrentMessageListenerContainer load(Long id) throws Exception {
                            //TODO load
                            return null;
                        }
                    });

    /**
     * 获取kafka监听者缓存
     *
     * @return 缓存对象
     */
    public LoadingCache<Long, ConcurrentMessageListenerContainer> getKafkaListenerCache() {
        return kafkaListenerCache;
    }
}
