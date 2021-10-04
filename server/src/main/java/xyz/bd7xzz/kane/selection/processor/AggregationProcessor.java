package xyz.bd7xzz.kane.selection.processor;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.selection.SelectionProcessor;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 聚合处理器
 * @date 2021/8/25 3:26 下午
 */
@Service("aggregationProcessor")
public class AggregationProcessor implements SelectionProcessor {
    @Override
    public boolean doProcess(TaskContextVO context) {
        return false;
    }
}
