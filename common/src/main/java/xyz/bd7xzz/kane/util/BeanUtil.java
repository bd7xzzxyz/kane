package xyz.bd7xzz.kane.util;

import org.springframework.cglib.beans.BeanCopier;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: bean处理工具类
 * @date 7/4/21 8:18 PM
 */
public class BeanUtil {

    /**
     * 拷贝对象
     *
     * @param source      源对象
     * @param targetClazz 目标类
     * @param <T>
     * @param <S>
     * @return 目标对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, S> T copy(S source, Class<T> targetClazz) throws IllegalAccessException, InstantiationException {
        if (null == source || null == targetClazz) {
            return null;
        }

        T t = targetClazz.newInstance();
        BeanCopier.create(source.getClass(), targetClazz, false).copy(
                source, t, null
        );
        return t;
    }
}
