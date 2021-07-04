package xyz.bd7xzz.kane.vo.driver;

import lombok.Data;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: kafka驱动
 * @date 7/4/21 12:15 PM
 */
@Data
public class KafkaDriverVO extends BasicDriverVO{

    private String topic;
    private String groupId;
    private String bootstrapServer;
    private long autoCommitInterval;
    private long requestTimeOut;
    private long sessionTimeOut;
}
