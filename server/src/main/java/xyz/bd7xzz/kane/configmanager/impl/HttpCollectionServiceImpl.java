package xyz.bd7xzz.kane.configmanager.impl;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.collection.impl.JSONDataHandlerImpl;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.CollectionDataSerializeTypeConstraint;
import xyz.bd7xzz.kane.constraint.ServiceHandler;
import xyz.bd7xzz.kane.exception.KaneRuntimException;
import xyz.bd7xzz.kane.serialize.HttpJSONSerializeHandler;
import xyz.bd7xzz.kane.util.DateUtil;
import xyz.bd7xzz.kane.vo.CollectionVO;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.HttpConnectionVO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author bd7xzz
 * @description: http请求业务处理
 * @date 2021/7/8 7:09 下午
 */
@Slf4j
@Service("httpCollectionServiceImpl")
public class HttpCollectionServiceImpl implements ServiceHandler {
    @Override
    public <P> void doService(P param) {
        if (!(param instanceof ConnectionVO)) {
            throw new KaneRuntimException("invalid connection vo class " + param.getClass() + ",target class is ConnectionVO");
        }
        ConnectionVO connectionVO = (ConnectionVO) param;
        Object connection = connectionVO.getConnection();
        if (!(connection instanceof HttpConnectionVO)) {
            throw new KaneRuntimException("invalid connection class " + param.getClass() + ",target class is HttpConnectionVO");
        }
        HttpConnectionVO httpConnection = (HttpConnectionVO) connection;
        Response response;
        String url = httpConnection.getRequest().url().url().toString();
        if (httpConnection.isRetry()) {
            Retryer<Response> retryer = RetryerBuilder.<Response>newBuilder()
                    .retryIfExceptionOfType(IOException.class)
                    .withStopStrategy(StopStrategies.stopAfterAttempt(httpConnection.getRetryCount()))
                    .withWaitStrategy(WaitStrategies.exponentialWait(3, 10, TimeUnit.SECONDS))
                    .build();
            try {
                response = retryer.call(() -> httpConnection.getHttpClient().newCall(httpConnection.getRequest()).execute());
            } catch (ExecutionException | RetryException e) {
                log.error("collect http data source error after retry {}! url:{}", httpConnection.getRetryCount(), url, e);
                return;
            }
        } else {
            try {
                response = httpConnection.getHttpClient().newCall(httpConnection.getRequest()).execute();
            } catch (IOException e) {
                log.error("collect http data source error! ur:{}", url, e);
                return;
            }
        }

        if (response.code() >= 300) {
            log.error("collect http data source error! response code:{} ur:{}", response.code(), url);
            return;
        }

        List<Class<?>> serializeClasses = CollectionDataSerializeTypeConstraint.getSerializeClasses(httpConnection.getSerializeType());
        try {
            for (Class<?> serializeClass : serializeClasses) {
                if (serializeClass.equals(HttpJSONSerializeHandler.class)) {
                    CollectionDataHandler collectionDataHandler = SpringContextUtil.getBean(JSONDataHandlerImpl.class);
                    collectionDataHandler.extract(CollectionVO.builder()
                            .from(connectionVO.getType())
                            .generationTime(DateUtil.getUnixTimestamp())
                            .jsonData(HttpJSONSerializeHandler.deserializer(response.body().bytes()))
                            .build());
                }
            }
        } catch (IOException e) {
            log.error("collect http data source error! get response bytes error ur:{}", url);
        }
    }
}
