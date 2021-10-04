package xyz.bd7xzz.kane.vo;

import xyz.bd7xzz.kane.constraint.SelectionTaskSignalConstraint;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author baodi1
 * @description: 筛选任务上下文
 * @date 2021/8/24 2:59 下午
 */
public class TaskContextVO {
    private SelectionTaskVO task;
    private SelectionConfigVO selectionConfig;
    private SelectionResultVO selectionResult;
    private String currentThreadName;
    private AtomicReference<SelectionTaskSignalConstraint> signalFlag;

    TaskContextVO(SelectionTaskVO task, SelectionConfigVO selectionConfig, SelectionResultVO selectionResult, String currentThreadName) {
        this.task = task;
        this.selectionConfig = selectionConfig;
        this.selectionResult = selectionResult;
        this.currentThreadName = currentThreadName;
        this.signalFlag = new AtomicReference<>(null);
    }

    public static TaskContextVOBuilder builder() {
        return new TaskContextVOBuilder();
    }

    public static class TaskContextVOBuilder {
        private SelectionTaskVO task;
        private SelectionConfigVO selectionConfig;
        private SelectionResultVO selectionResult;
        private String currentThreadName;

        TaskContextVOBuilder() {
        }

        public TaskContextVOBuilder task(SelectionTaskVO task) {
            this.task = task;
            return this;
        }

        public TaskContextVOBuilder selectionConfig(SelectionConfigVO selectionConfig) {
            this.selectionConfig = selectionConfig;
            return this;
        }

        public TaskContextVOBuilder selectionResult(SelectionResultVO selectionResult) {
            this.selectionResult = selectionResult;
            return this;
        }

        public TaskContextVOBuilder currentThreadName(String currentThreadName) {
            this.currentThreadName = currentThreadName;
            return this;
        }

        public TaskContextVO build() {
            return new TaskContextVO(task, selectionConfig, selectionResult, currentThreadName);
        }
    }

    public SelectionTaskVO getTask() {
        return task;
    }

    public SelectionConfigVO getSelectionConfig() {
        return selectionConfig;
    }

    public SelectionResultVO getSelectionResult() {
        return selectionResult;
    }

    public String getCurrentThreadName() {
        return currentThreadName;
    }

    public SelectionTaskSignalConstraint getSignalFlag() {
        if (signalFlag != null) {
            return signalFlag.get();
        }
        return null;
    }

    /**
     * cas设置信号位
     * @param signalConstraint 信号常量
     * @return true设置成功 false设置失败
     */
    public boolean setSignalFlag(SelectionTaskSignalConstraint signalConstraint) {
        return signalFlag.compareAndSet(null, signalConstraint);
    }

    public void setSelectionResult(SelectionResultVO selectionResult) {
        this.selectionResult = selectionResult;
    }
}
