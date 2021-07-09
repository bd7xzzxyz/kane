package xyz.bd7xzz.kane.util;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author baodi1
 * @description: 时间工具
 * @date 2021/7/9 4:29 下午
 */
public class DateUtil extends DateUtils {
    /**
     * 获取unix系统时间戳
     * @return
     */
    public static long getUnixTimestamp(){
        return System.currentTimeMillis()/1000;
    }
}
