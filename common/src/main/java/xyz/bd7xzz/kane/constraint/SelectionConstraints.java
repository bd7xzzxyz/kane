package xyz.bd7xzz.kane.constraint;

/**
 * @author baodi1
 * @description: 通用的约束
 * @date 2021/8/23 5:18 下午
 */
public class SelectionConstraints {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final short TASK_STATE_UNKNOWN = -1;
    public static final short TASK_STATE_RUNNING = 1;
    public static final short TASK_STATE_WAITING = 2;
    public static final short TASK_STATE_DONE = 3;
    public static final short TASK_STATE_PAUSED = 4;
    public static final short TASK_STATE_ERROR = 5;
    public static final int TASK_MESSAGE_CODE_SUCCESS = 0;
    public static final int TASK_MESSAGE_CODE_BIZ_ERROR = 1;
    public static final int TASK_MESSAGE_CODE_BIZ_JAVA_ERROR = 2;
    public static final int TASK_MESSAGE_CODE_MANUAL_STOP = 3;
    public static final int TASK_MESSAGE_CODE_MANUAL_PAUSE = 4;
    public static final String TASK_MESSAGE_CODE_STOPPED_MESSAGE = "manual stopped at %s";
    public static final String TASK_MESSAGE_CODE_PAUSED_MESSAGE = "manual paused at %s";
    public static final String TASK_ID_PREFIX = "task-";

    /**
     * 任务是否是最终状态
     *
     * @param state 状态值
     * @return true 是 false 否
     */
    public static boolean isFinalFinalState(short state) {
        return state != SelectionConstraints.TASK_STATE_DONE && state != SelectionConstraints.TASK_STATE_ERROR;
    }
}
