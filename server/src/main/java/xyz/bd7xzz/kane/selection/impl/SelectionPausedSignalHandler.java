package xyz.bd7xzz.kane.selection.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;
import xyz.bd7xzz.kane.selection.SelectionSignalHandler;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 任务暂停信号
 * @date 2021/10/4 6:35 下午
 */
@Component("pausedSignal")
@Slf4j
public class SelectionPausedSignalHandler implements SelectionSignalHandler {

    @Override
    public boolean handle(TaskContextVO taskContext) {
        //todo
        return SelectionTaskSignalConstraint.PAUSE.getHandlerResult();
    }
}
