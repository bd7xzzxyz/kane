package xyz.bd7xzz.kane.serialize;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: kafka序列化处理器
 * @date 7/4/21 6:09 PM
 */
public class KafkaJSONSerializeHandler {

    /**
     * 获取值反序列化处理器
     *
     * @return 反序列化处理器
     */
    public static Deserializer getValueDeserializer() {
        return new StringDeserializer();
    }

    /**
     * 反序列化数据
     *
     * @param data mq报文
     * @return json字符串
     */
    public static String deserializer(Object data) {
        if (null == data) {
            return StringUtils.EMPTY;
        }
        return data.toString();
    }
}