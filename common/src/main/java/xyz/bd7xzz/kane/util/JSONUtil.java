package xyz.bd7xzz.kane.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class JSONUtil {

    /**
     * 转换jsonObject
     *
     * @param json  json字符串
     * @param clazz 目标类
     * @param <T>
     * @return 目标对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (null == json || json.trim().equals("")) {
            return null;
        }
        return new Gson().fromJson(json, clazz);
    }

    /**
     * 转换jsonArray
     *
     * @param json  json字符串
     * @param clazz 目标类
     * @param <T>
     * @return 目标list对象
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (null == json || json.trim().equals("")) {
            return null;
        }

        return new Gson().fromJson(json, new TypeToken<T>() {
        }.getType());
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
     * 是否是json array
     *
     * @param json json字符串
     * @return true为是 false为 不是
     */
    public static boolean isJSONArray(String json) {
        JsonElement jsonElement = parseElement(json);
        if (null == jsonElement) {
            return false;
        }

        return jsonElement.isJsonArray();
    }

    /**
     * 是否是json object
     *
     * @param json json字符串
     * @return true为是 false为 不是
     */
    public static boolean isJSONOBject(String json) {
        JsonElement jsonElement = parseElement(json);
        if (null == jsonElement) {
            return false;
        }

        return jsonElement.isJsonObject();
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
