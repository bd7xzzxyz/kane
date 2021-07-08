package xyz.bd7xzz.kane.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author bd7xzz
 * @description: http链接
 * @date 2021/7/8 2:39 下午
 */
@Builder
@Getter
public class HttpConnectionVO {
    @Setter
    private OkHttpClient httpClient;
    @Setter
    private Request request;
    private boolean sharedConnection;
}
