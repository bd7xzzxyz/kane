package xyz.bd7xzz.kane.executor;

import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @description: 定时调度线程池
 * @date 2021/7/8 5:28 下午
 */
@Configuration
@Slf4j
public class ScheduledExecutor {

    @Bean("dataCollectExecutor")
    public ThreadPoolExecutor dataCollectExecutor() {
        int core = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(core * 4 + 1, core * 10,
                30, TimeUnit.SECONDS, Queues.newArrayBlockingQueue(500),
                (r, executor) -> log.error("dataCollectExecutor reject!"));
    }
}
