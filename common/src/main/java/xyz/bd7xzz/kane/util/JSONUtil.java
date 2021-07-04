package xyz.bd7xzz.kane.util;

import com.google.gson.Gson;

public class JSONUtil {

    /**
     * 转换jsonObject
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (null == json || json.equals("")) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}
