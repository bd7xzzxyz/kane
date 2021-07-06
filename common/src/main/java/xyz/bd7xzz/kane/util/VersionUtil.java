package xyz.bd7xzz.kane.util;

import java.util.UUID;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 版本工具类
 * @date 7/6/21 8:41 PM
 */
public class VersionUtil {

    /**
     * 生成版本号
     *
     * @return 版本号
     */
    public static String generateVersion() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
