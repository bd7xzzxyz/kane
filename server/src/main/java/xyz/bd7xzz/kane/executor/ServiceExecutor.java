package xyz.bd7xzz.kane.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author baodi1
 * @description: 业务线程池
 * @date 2021/8/23 5:05 下午
 */
@Configuration
@Slf4j
public class ServiceExecutor {

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(5000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadGroupName("task-executor");
        executor.setRejectedExecutionHandler((r, currentExecutor) -> log.error("taskExecutor reject"));
        return executor;
    }
}
