package xyz.bd7xzz.kane.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 拷贝对象集合
     *
     * @param source      源对象集合
     * @param targetClazz 目标类
     * @param <T>
     * @param <S>
     * @return 目标对象集合
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T, S> List<T> copy(List<S> source, Class<T> targetClazz) throws InstantiationException, IllegalAccessException {
        if (null == source || source.size() == 0 || null == targetClazz) {
            return new ArrayList<>(0);
        }

        List<T> results = new ArrayList<>(source.size());
        for (S s : source) {
            results.add(copy(s, targetClazz));
        }
        return results;
    }

}
