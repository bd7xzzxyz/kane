package xyz.bd7xzz.kane.collection.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.constraint.CollectionFieldTypeConstraint;
import xyz.bd7xzz.kane.constraint.CollectionRequestMethodConstraint;
import xyz.bd7xzz.kane.constraint.ServiceHandler;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.CollectionVO;
import xyz.bd7xzz.kane.vo.TableVO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: json数据处理器
 * @date 7/4/21 5:53 PM
 */
@Service
@Slf4j
public class JSONDataHandlerImpl extends CollectionDataHandler {

    @Override
    public void extract(CollectionVO collectionVO, ServiceHandler handler) {
        String json = collectionVO.getJsonData();
        if (StringUtils.isEmpty(json)) {
            return;
        }

        List<CollectionFieldVO> collectionFields = getCollectionFields(collectionVO.getDataSourceId());
        if (CollectionUtils.isEmpty(collectionFields)) {
            log.warn("cannot found data source id = {} collection fields", collectionVO.getDataSourceId());
            return;
        }

        Map<String, CollectionFieldVO> mapper = collectionFields.stream().collect(Collectors.toMap(CollectionFieldVO::getName, Function.identity()));
        String pkName = getPrimaryKeySourceFieldName(collectionFields);
        JsonElement jsonElement = JSONUtil.parseElement(collectionVO.getJsonData());
        Table<Long, String, Object> table = null;
        short method = 0;
        if (jsonElement.isJsonObject()) {
            table = HashBasedTable.create(1, mapper.size());
            JsonObject object = jsonElement.getAsJsonObject();
            method = object.get(CollectionRequestMethodConstraint.REQUEST_METHOD_KEY).getAsShort();
            extractJSONObject(mapper, object, table, pkName);
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            method = array.get(0).getAsJsonObject().get(CollectionRequestMethodConstraint.REQUEST_METHOD_KEY).getAsShort();
            table = HashBasedTable.create(array.size(), mapper.size());
            for (JsonElement element : array) {
                extractJSONObject(mapper, element.getAsJsonObject(), table, pkName);
            }
        }
        handler.doService(TableVO.builder()
                .method(method)
                .table(table)
                .build());
    }

    /**
     * 抽取json object中的值，执行业务处理
     *
     * @param mapper 字段名和CollectionFieldVO的映射
     * @param object jsonObject对象
     * @param table  表
     * @param pkName 主键名
     */
    private void extractJSONObject(Map<String, CollectionFieldVO> mapper, JsonObject object, Table<Long, String, Object> table, String pkName) {
        if (object.has(pkName)) {
            throw new IllegalArgumentException("cannot found primary key value");
        }
        long pkValue = object.get(pkName).getAsLong();
        mapper.forEach((sourceField, collectionFieldVO) -> {
            JsonElement fieldElement = object.get(sourceField);
            if (CollectionFieldTypeConstraint.FLOAT.equals(collectionFieldVO.getType())) {
                float value = fieldElement.getAsFloat();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.DOUBLE.equals(collectionFieldVO.getType())) {
                double value = fieldElement.getAsDouble();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.INT.equals(collectionFieldVO.getType())) {
                int value = fieldElement.getAsInt();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.LONG.equals(collectionFieldVO.getType())) {
                long value = fieldElement.getAsLong();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.CHAR.equals(collectionFieldVO.getType())) {
                char value = fieldElement.getAsString().toCharArray()[0];
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.STRING.equals(collectionFieldVO.getType())) {
                String value = fieldElement.getAsString();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.BOOL.equals(collectionFieldVO.getType())) {
                boolean value = fieldElement.getAsBoolean();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            } else if (CollectionFieldTypeConstraint.BYTE.equals(collectionFieldVO.getType())) {
                byte value = fieldElement.getAsByte();
                table.put(pkValue, collectionFieldVO.getTargetField(), value);
            }
        });
    }

}
