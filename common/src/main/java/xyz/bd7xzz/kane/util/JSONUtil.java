package xyz.bd7xzz.kane.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JSONUtil {

    /**
     * 转换jsonObject
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (null == json || json.trim().equals("")) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * 校验json是否有效
     *
     * @param json
     * @return
     */
    public static boolean validate(String json) {
        if (null == json || json.trim().equals("")) {
            return false;
        }
        try {
            JsonElement jsonElement = JsonParser.parseString(json);
            return jsonElement != null;
        } catch (JsonParseException e) {
            return false;
        }
    }
}
