package xyz.bd7xzz.kane.selection;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.vo.TaskContextVO;

import java.util.Map;

/**
 * @author baodi1
 * @description: 内部调度
 * @date 2021/10/4 8:23 下午
 */
@EnableScheduling
@Component
public class InternalScheduled {
    private final LocalCache localCache;

    public InternalScheduled(LocalCache localCache) {
        this.localCache = localCache;
    }

    @Scheduled(fixedRate = 30000)
    public void checkSelectionTaskThread() {
        for (Map.Entry<Long, TaskContextVO> entry : localCache.getTaskContextCache().entrySet()) {
            SelectionSignalRegister.checkAndExecute(entry.getValue(), false);
        }
    }
}
