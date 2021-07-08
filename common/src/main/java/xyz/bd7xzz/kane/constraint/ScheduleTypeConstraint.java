package xyz.bd7xzz.kane.constraint;

/**
 * @author bd7xzz
 * @description: 调度类型
 * @date 2021/7/8 6:58 下午
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScheduleTypeConstraint {
    DATA_COLLECT("dataCollectExecutor");
    private String executor;
}
