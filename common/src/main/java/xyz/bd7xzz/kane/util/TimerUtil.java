package xyz.bd7xzz.kane.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @description: 定时器
 * @date 2021/7/7 1:17 下午
 */
public class TimerUtil {
    private static final Timer DAY = new HashedWheelTimer();
    private static final Timer HOUR = new HashedWheelTimer();
    private static final Timer MINUTE = new HashedWheelTimer();
    private static final Timer SECOND = new HashedWheelTimer();
    private static final Map<String, Boolean> REMOVED_TASKS = new ConcurrentHashMap<>();

    public static class Task {
        private int day;
        private int hour;
        private int minute;
        private int second;
        private TaskExecutor runnable;
        private boolean once;
        private String threadName;
        private Executor executor;

        public static class Builder {
            private int hour;
            private int minute;
            private int second;
            private int day;
            private TaskExecutor runnable;
            private boolean once;
            private String threadName;
            private Executor executor;

            /**
             * 设置触发时间-天级
             *
             * @param day 天
             * @return build对象
             */
            public Builder day(int day) {
                this.day = day;
                return this;
            }

            /**
             * 设置触发时间-小时级
             *
             * @param hour 小时
             * @return build对象
             */
            public Builder hour(int hour) {
                this.hour = hour;
                return this;
            }

            /**
             * 设置触发时间-分钟级
             *
             * @param minute 分钟
             * @return build对象
             */
            public Builder minute(int minute) {
                this.minute = minute;
                return this;
            }

            /**
             * 设置触发时间-秒级
             *
             * @param second 秒
             * @return build对象
             */
            public Builder second(int second) {
                this.second = second;
                return this;
            }

            /**
             * 设置执行的任务
             *
             * @param runnable TaskExecutor对象
             * @return build对象
             */
            public Builder runnable(TaskExecutor runnable) {
                this.runnable = runnable;
                return this;
            }

            /**
             * 设置只执行一次任务
             *
             * @param once true为只执行一次，false为循环执行，默认false
             * @return build对象
             */
            public Builder once(boolean once) {
                this.once = once;
                return this;
            }

            /**
             * 设置工作线程名
             *
             * @param threadName 线程名
             * @return build对象
             */
            public Builder threadName(String threadName) {
                this.threadName = threadName;
                return this;
            }

            /**
             * 设置执行任务的线程池
             *
             * @param executor 线程池，默认为Executors.newCachedThreadPool()
             * @return build对象
             */
            public Builder executor(Executor executor) {
                this.executor = executor;
                return this;
            }

            /**
             * 构建Task对象
             *
             * @return task对象
             */
            public Task build() {
                return new Task(this);
            }
        }

        private Task(Builder builder) {
            this.day = builder.day;
            this.hour = builder.hour;
            this.minute = builder.minute;
            this.second = builder.second;
            this.runnable = builder.runnable;
            this.once = builder.once;
            this.threadName = builder.threadName;
            this.executor = builder.executor;
        }

        /**
         * 创建builder
         *
         * @return Task.Builder对象
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * 获取触发时间-小时级
         *
         * @return 小时
         */
        public int getHour() {
            return hour;
        }

        /**
         * 获取触发时间-分钟级
         *
         * @return 分钟
         */
        public int getMinute() {
            return minute;
        }

        /**
         * 获取触发时间-秒级
         *
         * @return 秒
         */
        public int getSecond() {
            return second;
        }

        /**
         * 获取执行的任务
         *
         * @return TaskExecutor对象
         */
        public TaskExecutor getRunnable() {
            return runnable;
        }

        /**
         * 是否一次执行
         *
         * @return true 为一次执行 false为循环执行
         */
        public boolean isOnce() {
            return once;
        }

        /**
         * 获取工作线程名
         *
         * @return 线程名
         */
        public String getThreadName() {
            return threadName;
        }

        /**
         * 获取执行任务的线程池，默认为Executors.newCachedThreadPool()
         *
         * @return
         */
        public Executor getExecutor() {
            return executor;
        }

        /**
         * 获取触发时间-天级
         *
         * @return 天
         */
        public int getDay() {
            return day;
        }

        /**
         * 设置执行任务的线程池
         *
         * @param executor 线程池，默认为Executors.newCachedThreadPool()
         */
        public void setExecutor(Executor executor) {
            this.executor = executor;
        }

        /**
         * 设置触发时间-天级
         *
         * @param day 天
         */
        public void setDay(int day) {
            this.day = day;
        }

        /**
         * 设置触发时间-小时级
         *
         * @param hour 小时
         */
        public void setHour(int hour) {
            this.hour = hour;
        }

        /**
         * 设置触发时间-分钟级
         *
         * @param minute 分钟
         */
        public void setMinute(int minute) {
            this.minute = minute;
        }

        /**
         * 设置触发时间-秒级
         *
         * @param second 秒
         */
        public void setSecond(int second) {
            this.second = second;
        }
    }

    public static abstract class TaskExecutor implements Runnable {
        private String taskId;

        /**
         * 获取任务id
         *
         * @return 任务id
         */
        public String getTaskId() {
            return taskId;
        }

        /**
         * 设置任务id
         *
         * @param taskId 任务id
         */
        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }


    /**
     * 移除任务
     *
     * @param taskId 任务id
     */
    public static void removeTask(String taskId) {
        if (REMOVED_TASKS.containsKey(taskId)) {
            REMOVED_TASKS.put(taskId, true);
        }
    }

    /**
     * 执行定时任务
     *
     * @param task 任务
     * @return 任务id
     */
    public static String executeTask(Task task) {
        if (null == task || null == task.getRunnable()) {
            throw new IllegalArgumentException("task cannot be null!");
        }
        if (null == task.getExecutor()) {
            task.setExecutor(Executors.newCachedThreadPool());
        }
        final String taskId = UUID.randomUUID().toString().replace("-", "");
        REMOVED_TASKS.put(taskId, false);
        DAY.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                final TimerTask firstTask = this;
                HOUR.newTimeout(hourTimeout ->
                        MINUTE.newTimeout(minTimeout ->
                                        SECOND.newTimeout(secTimeout -> {
                                            if (REMOVED_TASKS.containsKey(taskId) && REMOVED_TASKS.get(taskId)) {
                                                REMOVED_TASKS.remove(taskId);
                                                return;
                                            }
                                            if (!task.isOnce()) {
                                                timeout.timer().newTimeout(firstTask, task.getHour(), TimeUnit.HOURS);
                                            }
                                            task.getRunnable().setTaskId(taskId);
                                            task.getExecutor().execute(() -> {
                                                Thread.currentThread().setName(task.getThreadName());
                                                task.getRunnable().run();
                                            });
                                        }, task.getSecond(), TimeUnit.SECONDS)
                                , task.getMinute(), TimeUnit.MINUTES), task.getHour(), TimeUnit.HOURS);
            }
        }, task.getDay(), TimeUnit.DAYS);
        return taskId;
    }

}
