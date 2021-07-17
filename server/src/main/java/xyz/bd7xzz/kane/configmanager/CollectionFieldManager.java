package xyz.bd7xzz.kane.configmanager;

import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段服务
 * @date 7/14/21 10:00 PM
 */
public interface CollectionFieldManager {
    /**
     * 为数据源添加采集字段
     *
     * @param collectionFields    采集字段
     * @param dataSourceConfigVOS 数据源vo
     * @return 采集字段id
     */
    List<Long> addCollectionField(List<CollectionFieldVO> collectionFields, List<DataSourceConfigVO> dataSourceConfigVOS);

    /**
     * 根据id获取采集字段
     *
     * @param id 采集字段id
     * @return 采集字段vo对象
     */
    CollectionFieldVO getById(long id);

    /**
     * 删除采集字段
     *
     * @param id 采集字段id
     */
    void deleteCollectionField(long id);

    /**
     * 按数据源获取采集字段
     *
     * @param dataSourceId 数据源id
     * @return 采集字段
     */
    List<CollectionFieldVO> getCollectionFieldByDataSourceId(long dataSourceId);

    /**
     * 修改采集字段
     *
     * @param collectionFieldVO  采集字段
     * @param dataSourceConfigVO 数据源配置
     */
    void updateCollectionField(CollectionFieldVO collectionFieldVO, DataSourceConfigVO dataSourceConfigVO);
}
