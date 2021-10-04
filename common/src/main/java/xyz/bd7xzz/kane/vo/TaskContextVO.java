package xyz.bd7xzz.kane.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author baodi1
 * @description: 筛选任务上下文
 * @date 2021/8/24 2:59 下午
 */
@Builder
@Getter
public class TaskContextVO {
    private SelectionTaskVO task;
    private SelectionConfigVO selectionConfig;
}
