package xyz.bd7xzz.kane.configmanager.impl;

import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.exception.KaneRuntimException;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.HttpConnectionVO;
import xyz.bd7xzz.kane.vo.driver.HttpDriverVO;

import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @description: http链接处理器
 * @date 2021/7/8 1:28 下午
 */
@Service("httpConnectionHandler")
public class HttpConnectionHandler extends ConnectionHandler {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient SHARED_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build();

    @Override
    protected <T> ConnectionVO createConnection(T driverVO) {
        if (!(driverVO instanceof HttpDriverVO)) {
            throw new KaneRuntimException("invalid driverVO class " + driverVO.getClass() + ",target class is HttpDriverVO");
        }
        HttpDriverVO httpDriverVO = (HttpDriverVO) driverVO;
        OkHttpClient httpClient = getOkHttpClient(httpDriverVO);
        Request.Builder request = new Request.Builder();
        if (MapUtils.isNotEmpty(httpDriverVO.getHeaders())) {
            httpDriverVO.getHeaders().forEach((s, o) -> request.addHeader(s, o.toString()));
        }
        if (StringUtils.isNotEmpty(httpDriverVO.getBody())) {
            RequestBody requestBody = RequestBody.create(httpDriverVO.getBody(), JSON);
            if (httpDriverVO.getMethod().equals(HttpDriverVO.METHOD_PUT)) {
                request.put(requestBody);
            } else if (httpDriverVO.getMethod().equals(HttpDriverVO.METHOD_POST)) {
                request.post(requestBody);
            }
        }
        return ConnectionVO.builder()
                .id(httpDriverVO.getId())
                .type(httpDriverVO.getType())
                .connection(HttpConnectionVO.builder()
                        .serializeType(httpDriverVO.getSerializeType())
                        .sharedConnection(httpDriverVO.isSharedConnection())
                        .httpClient(httpClient)
                        .request(request.build())
                        .retryCount(httpDriverVO.getRetryCount())
                        .retry(httpDriverVO.isRetry())
                        .build())
                .build();
    }

    @Override
    protected void releaseConnection(ConnectionVO connectionVO) {
        if (!(connectionVO.getConnection() instanceof HttpConnectionVO)) {
            return;
        }

        HttpConnectionVO httpConnection = (HttpConnectionVO) connectionVO.getConnection();
        if (!httpConnection.isSharedConnection()) {
            httpConnection.setHttpClient(null);
            httpConnection.setRequest(null);
        }
    }

    /**
     * 获取OkHttp客户端
     *
     * @param httpDriverVO http驱动
     * @return OkHttpClient对象
     */
    private OkHttpClient getOkHttpClient(HttpDriverVO httpDriverVO) {
        if (httpDriverVO.isSharedConnection()) {
            return SHARED_HTTP_CLIENT;
        }
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(httpDriverVO.isRetryOnConnectionFailure())
                .connectTimeout(httpDriverVO.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(httpDriverVO.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpDriverVO.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
}
