package xyz.bd7xzz.kane.selection.processor;

import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.selection.SelectionProcessor;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 语法优化
 * @date 2021/8/24 3:52 下午
 */
@Service("syntaxOptimizerProcessor")
public class SyntaxOptimizerProcessor implements SelectionProcessor {

    @Override
    public boolean doProcess(TaskContextVO context) {
        return true;
    }
}
