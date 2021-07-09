package xyz.bd7xzz.kane.util;

import com.google.gson.*;

public class JSONUtil {

    /**
     * 转换jsonObject
     *
     * @param json json字符串
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
     * 转换jsonObject
     *
     * @param json json字符串
     * @return jsonObject对象
     */
    public static JsonObject parseObject(String json) {
        if (null == json || json.length() == 0) {
            return null;
        }
        return new Gson().fromJson(json, JsonObject.class);
    }

    /**
     * 转换jsonElement对象
     *
     * @param json json字符串
     * @return jsonElement对象
     */
    public static JsonElement parseElement(String json) {
        if (null == json || json.length() == 0) {
            return null;
        }
        return new Gson().fromJson(json, JsonElement.class);
    }


    /**
     * 转换jsonArray
     *
     * @param json json字符串
     * @return jsonArray对象
     */
    public static JsonArray parseArray(String json) {
        if (null == json || json.length() == 0) {
            return new JsonArray(0);
        }
        return new Gson().fromJson(json, JsonArray.class);
    }

    /**
     * 校验json是否有效
     *
     * @param json json字符串
     * @return true 有效 false 无效
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
