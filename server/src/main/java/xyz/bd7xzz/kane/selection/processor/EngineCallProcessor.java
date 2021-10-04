package xyz.bd7xzz.kane.selection.processor;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.selection.SelectionProcessor;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 引擎调度
 * @date 2021/8/24 3:54 下午
 */
@Service("engineCallProcessor")
public class EngineCallProcessor implements SelectionProcessor {

    @Override
    public boolean doProcess(TaskContextVO context) {
        return true;
    }
}
