package xyz.bd7xzz.kane.vo.driver;

import lombok.Data;

import java.util.Map;

/**
 * @author bd7xzz
 * @description: http驱动
 * @date 2021/7/8 1:27 下午
 */
@Data
public class HttpDriverVO extends BasicDriverVO {
    public static final String METHOD_POST = "post";
    public static final String METHOD_GET = "get";
    public static final String METHOD_PUT = "put";

    private String requestUrl;
    private Map<String, Object> headers;
    private String method;
    private String body;
    private long connectTimeout = 500;
    private long readTimeout = 1000;
    private long writeTimeout = 1000;
    private boolean sharedConnection = true;
    private boolean retryOnConnectionFailure = true;
    private boolean retry;
    private int retryCount = 3;
}
