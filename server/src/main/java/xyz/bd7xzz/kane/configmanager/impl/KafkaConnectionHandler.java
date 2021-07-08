package xyz.bd7xzz.kane.configmanager.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.collection.impl.JSONDataHandlerImpl;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.configmanager.ConnectionHandler;
import xyz.bd7xzz.kane.constraint.CollectionDataSerializeTypeConstraint;
import xyz.bd7xzz.kane.constraint.DataSourceTypeConstraint;
import xyz.bd7xzz.kane.exception.KaneRuntimException;
import xyz.bd7xzz.kane.serialize.KafkaJSONSerializeHandler;
import xyz.bd7xzz.kane.vo.CollectionVO;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.driver.KafkaDriverVO;

import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: kafka驱动处理器
 * @date 7/4/21 12:28 PM
 */
@Service("kafkaDriverHandler")
public class KafkaConnectionHandler extends ConnectionHandler {

    @Override
    protected <T> ConnectionVO createConnection(T driverVO) {
        if (!(driverVO instanceof KafkaDriverVO)) {
            throw new KaneRuntimException("invalid driverVO class " + driverVO.getClass() + ",target class is KafkaDriverVO");
        }
        KafkaDriverVO kafkaDriverVO = (KafkaDriverVO) driverVO;
        List<Class<?>> serializeClasses = CollectionDataSerializeTypeConstraint.getSerializeClasses(kafkaDriverVO.getSerializeType());
        if (CollectionUtils.isEmpty(serializeClasses)) {
            throw new IllegalArgumentException("invalid serialize type");
        }
        DefaultKafkaConsumerFactory kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(
                buildConsumerConfig(kafkaDriverVO),
                new StringDeserializer(),
                getValueDeserializer(serializeClasses)
        );
        ContainerProperties containerProperties = new ContainerProperties(kafkaDriverVO.getTopic());
        containerProperties.setMessageListener((MessageListener<Object, Object>) record -> {
            for (Class<?> serializeClass : serializeClasses) {
                if (serializeClass.equals(KafkaJSONSerializeHandler.class)) {
                    CollectionDataHandler collectionDataHandler = SpringContextUtil.getBean(JSONDataHandlerImpl.class);
                    collectionDataHandler.extract(CollectionVO.builder()
                            .from(DataSourceTypeConstraint.REAL_TIME_KAFKA.getType())
                            .generationTime(record.timestamp())
                            .jsonData(KafkaJSONSerializeHandler.deserializer(record.value()))
                            .build());
                }
            }
        });
        ConcurrentMessageListenerContainer container = createContainer(kafkaConsumerFactory, containerProperties);
        return ConnectionVO.builder()
                .id(kafkaDriverVO.getId())
                .type(kafkaDriverVO.getType())
                .connection(container)
                .build();
    }

    @Override
    protected void releaseConnection(ConnectionVO connectionVO) {
        Object connection = connectionVO.getConnection();
        if (!(connection instanceof ConcurrentMessageListenerContainer)) {
            throw new KaneRuntimException("invalid connection class " + connection.getClass() + ",target class is ConcurrentMessageListenerContainer");
        }
        ((ConcurrentMessageListenerContainer) connection).stop(true);
    }

    /**
     * 创建kafka container
     *
     * @param kafkaConsumerFactory 消费者工厂
     * @param containerProperties  container配置
     * @return 消费者container
     */
    public ConcurrentMessageListenerContainer createContainer(DefaultKafkaConsumerFactory kafkaConsumerFactory, ContainerProperties containerProperties) {
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer<>(kafkaConsumerFactory, containerProperties);
        container.start();
        return container;
    }

    /**
     * 获取值反序列化处理器
     *
     * @param serializeClasses 处理器类
     * @return 反序列化处理器
     */
    private Deserializer<?> getValueDeserializer(List<Class<?>> serializeClasses) {
        Deserializer<?> valueDeserializer = null;
        for (Class<?> serializeClass : serializeClasses) {
            if (serializeClass.equals(KafkaJSONSerializeHandler.class)) {
                valueDeserializer = KafkaJSONSerializeHandler.getValueDeserializer();
            }
        }
        if (valueDeserializer == null) {
            throw new IllegalArgumentException("invalid serialize type");
        }
        return valueDeserializer;
    }

    /**
     * 构建消费者配置参数
     *
     * @param kafkaDriverVO 驱动参数
     * @return 参数映射
     */
    private Map<String, Object> buildConsumerConfig(KafkaDriverVO kafkaDriverVO) {
        Map<String, Object> consumerConfig = Maps.newHashMap();
        consumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, kafkaDriverVO.getBootstrapServer());
        consumerConfig.put(GROUP_ID_CONFIG, kafkaDriverVO.getGroupId());
        consumerConfig.put(ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerConfig.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaDriverVO.getAutoCommitInterval());
        consumerConfig.put(AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerConfig.put(REQUEST_TIMEOUT_MS_CONFIG, kafkaDriverVO.getRequestTimeOut());
        consumerConfig.put(SESSION_TIMEOUT_MS_CONFIG, kafkaDriverVO.getSessionTimeOut());
        return consumerConfig;
    }

}
