package xyz.bd7xzz.kane.serialize;

import com.google.gson.JsonElement;
import xyz.bd7xzz.kane.util.JSONUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author baodi1
 * @description: http 序列化
 * @date 2021/7/9 4:19 下午
 */
public class HttpJSONSerializeHandler {

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @return jsonElement对象
     */
    public static JsonElement deserializer(byte[] bytes) {
        return JSONUtil.parseElement(new String(bytes, StandardCharsets.UTF_8));
    }
}
