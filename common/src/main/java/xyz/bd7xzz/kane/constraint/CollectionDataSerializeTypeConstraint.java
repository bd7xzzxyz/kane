package xyz.bd7xzz.kane.constraint;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.bd7xzz.kane.serialize.HttpJSONSerializeHandler;
import xyz.bd7xzz.kane.serialize.KafkaJSONSerializeHandler;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集数据序列化类型
 * @date 7/4/21 6:04 PM
 */
@Getter
@AllArgsConstructor
public enum CollectionDataSerializeTypeConstraint {

    JSON(0, "json", Lists.newArrayList(KafkaJSONSerializeHandler.class, HttpJSONSerializeHandler.class));
    private int type;
    private String name;
    private List<Class<?>> serializeClasses;

    /**
     * 根据类型获取序列化处理器类
     *
     * @param type
     * @return
     */
    public static List<Class<?>> getSerializeClasses(int type) {
        for (CollectionDataSerializeTypeConstraint value : CollectionDataSerializeTypeConstraint.values()) {
            if (value.type == type) {
                return value.serializeClasses;
            }
        }
        return Lists.newArrayListWithCapacity(0);
    }
}
