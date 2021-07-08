package xyz.bd7xzz.kane.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bd7xzz
 * @description: 链接信息
 * @date 2021/7/6 3:59 下午
 */
@Builder
@Getter
public class ConnectionVO {
    private long id;
    private int type;
    private Object connection;
    @Setter
    private String version;
}
