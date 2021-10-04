package xyz.bd7xzz.kane.constraint;

/**
 * @author baodi1
 * @description: 通用的约束
 * @date 2021/8/23 5:18 下午
 */
public class SelectionConstraints {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final short TASK_STATUS_UNKNOWN = -1;
    public static final short TASK_STATUS_RUNNING = 1;
    public static final short TASK_STATUS_WAITING = 2;
    public static final short TASK_STATUS_DONE = 3;
    public static final short TASK_STATUS_PAUSED = 4;
    public static final short TASK_STATUS_ERROR = 5;
    public static final int TASK_MESSAGE_CODE_SUCCESS = 0;
    public static final int TASK_MESSAGE_CODE_BIZ_ERROR = 1;
    public static final int TASK_MESSAGE_CODE_BIZ_JAVA_ERROR = 2;
    public static final String TASK_ID_PREFIX="task-";
}
