package xyz.bd7xzz.kane.serialize;

import com.google.gson.JsonElement;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import xyz.bd7xzz.kane.util.JSONUtil;

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
    public static JsonElement deserializer(Object data) {
        if (null == data) {
            return null;
        }
        return JSONUtil.parseElement(data.toString());
    }
}