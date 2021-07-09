package xyz.bd7xzz.kane.serialize;

import org.apache.commons.lang3.ArrayUtils;
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
    public static String deserializer(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new IllegalArgumentException("invalid ");
        }
        String json = new String(bytes, StandardCharsets.UTF_8);
        if (JSONUtil.validate(json)) {
            throw new IllegalArgumentException("invalid json!");
        }
        return json;
    }
}
