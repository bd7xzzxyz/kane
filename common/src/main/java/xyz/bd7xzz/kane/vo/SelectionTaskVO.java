package xyz.bd7xzz.kane.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 筛选任务
 * @date 7/25/21 10:46 PM
 */
@Builder
@Getter
public class SelectionTaskVO {
    private long taskId;
    private long configId;
    @Setter
    private short status;
    private Message successMessage;
    private Message errorMessage;
    private String executeTime;
    private String nextExecuteTime;

    @Builder
    @Getter
    public static class Message {
        private int code;
        private String message;
    }
}
