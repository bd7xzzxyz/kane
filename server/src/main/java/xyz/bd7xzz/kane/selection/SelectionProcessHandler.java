package xyz.bd7xzz.kane.selection;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;
import xyz.bd7xzz.kane.exception.KaneRuntimeException;
import xyz.bd7xzz.kane.selection.processor.*;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 筛选处理器
 * @date 2021/8/24 2:41 下午
 */
@Slf4j
public class SelectionProcessHandler {

    @AllArgsConstructor
    @Getter
    private enum SelectionProcessors {
        SYNTAX_CHECKER("syntax_checker", SyntaxCheckProcessor.class),
        SYNTAX_PARSER("syntax_parser", SyntaxParserProcessor.class),
        SYNTAX_OPTIMIZER("syntax_optimizer", SyntaxOptimizerProcessor.class),
        ENGINE_CALLER("engine_caller", EngineCallProcessor.class),
        AGGREGATION("aggregation", AggregationProcessor.class);
        private String name;
        private Class<? extends SelectionProcessor> processClass;
    }


    /**
     * 执行处理器
     *
     * @param context 上下文
     */
    public static void doProcess(TaskContextVO context) {
        if (null == context) {
            throw new KaneRuntimeException("invalid task context");
        }
        SelectionSignalRegister.register(SelectionTaskSignalConstraint.getAllSign(),context.getTask().getTaskId());
        for (SelectionProcessors value : SelectionProcessors.values()) {
            log.info("{} SelectionProcessHandler doProcess {} begin", context.getCurrentThreadName(), value.getName());
            if (!SpringContextUtil.getBean(value.getProcessClass()).doProcess(context)) {
                log.info("{} SelectionProcessHandler doProcess {} stop", context.getCurrentThreadName(), value.getName());
                return;
            }
            log.info("{} SelectionProcessHandler doProcess {} finish", context.getCurrentThreadName(), value.getName());
        }
    }
}
