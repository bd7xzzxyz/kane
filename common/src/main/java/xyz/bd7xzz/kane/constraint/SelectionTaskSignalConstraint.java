package xyz.bd7xzz.kane.constraint;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author baodi1
 * @description: 筛选任务信号
 * @date 2021/10/4 4:34 下午
 */
@AllArgsConstructor
public enum SelectionTaskSignalConstraint {

    STOPPED("stoppedSignal", false),
    PAUSE("pausedSignal", false),
    RESUME("resumedSignal", true);
    @Getter
    private String handlerName;
    private boolean handlerResult;
    private static final ImmutableList<SelectionTaskSignalConstraint> ALL;

    static {
        ImmutableList.Builder builder = ImmutableList.<SelectionTaskSignalConstraint>builder();
        for (SelectionTaskSignalConstraint value : SelectionTaskSignalConstraint.values()) {
            builder.add(value);
        }
        ALL = builder.build();
    }

    /**
     * 获取所有信号
     *
     * @return
     */
    public static List<SelectionTaskSignalConstraint> getAllSign() {
        return ALL;
    }

    /**
     * 处理器执行结果
     *
     * @return
     */
    public boolean getHandlerResult() {
        return handlerResult;
    }
}
