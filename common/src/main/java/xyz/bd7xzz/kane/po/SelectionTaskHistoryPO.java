package xyz.bd7xzz.kane.po;

import lombok.Data;

/**
 * @author baodi1
 * @description: 筛选任务历史
 * @date 2021/10/4 2:41 下午
 */
@Data
public class SelectionTaskHistoryPO {

    private long id;
    private long taskId;
    private short status;
    private long executeTime;
    private String errorMessage;
    private String successMessage;
    private int code;

}
