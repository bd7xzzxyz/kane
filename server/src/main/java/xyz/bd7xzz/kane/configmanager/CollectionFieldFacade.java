package xyz.bd7xzz.kane.configmanager;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段门面
 * @date 7/14/21 9:17 PM
 */
@Service
public class CollectionFieldFacade {

    private final CollectionFieldManager collectionFieldManager;
    private final DataSourceConfigManager dataSourceConfigManager;

    @Autowired
    public CollectionFieldFacade(CollectionFieldManager collectionFieldManager, DataSourceConfigManager dataSourceConfigManager) {
        this.collectionFieldManager = collectionFieldManager;
        this.dataSourceConfigManager = dataSourceConfigManager;
    }

    /**
     * 为数据源添加采集字段
     *
     * @param collectionFields 采集字段vo
     * @return ResponseVO 创建后的采集字段id列表
     */
    public ResponseVO addCollectionField(List<CollectionFieldVO> collectionFields) {
        if (CollectionUtils.isEmpty(collectionFields)) {
            throw new IllegalArgumentException("invalid collection fields");
        }

        List<Long> dsIdList = collectionFields.stream().map(CollectionFieldVO::getDataSourceId).collect(Collectors.toList());
        List<DataSourceConfigVO> dataSourceConfigVOS = dataSourceConfigManager.batchGetDataSourceById(dsIdList);
        List<Long> fieldIds = collectionFieldManager.addCollectionField(collectionFields, dataSourceConfigVOS);
        return ResponseVO.buildSuccess(fieldIds);
    }

    /**
     * 更新采集字段
     *
     * @param collectionFieldVO 采集字段vo
     * @return ResponseVO对象
     */
    public ResponseVO updateCollectionField(CollectionFieldVO collectionFieldVO) {
        DataSourceConfigVO dataSourceConfigVO = dataSourceConfigManager.getDataSourceById(collectionFieldVO.getDataSourceId());
        collectionFieldManager.updateCollectionField(collectionFieldVO, dataSourceConfigVO);
        return ResponseVO.buildSuccess();
    }

    /**
     * 获取采集字段
     *
     * @param id 采集字段id
     * @return ResponseVO 采集字段
     */
    public ResponseVO getCollectionField(long id) {
        return ResponseVO.buildSuccess(collectionFieldManager.getById(id));
    }

    /**
     * 删除采集字段
     *
     * @param id 采集字段id
     * @return ResponseVO对象
     */
    public ResponseVO deleteCollectionField(long id) {
        collectionFieldManager.deleteCollectionField(id);
        return ResponseVO.buildSuccess();
    }

    /**
     * 根据数据源id获取所有采集字段
     *
     * @param dataSourceId 数据源id
     * @return 所有采集字段
     */
    public ResponseVO getCollectionFieldByDataSourceId(long dataSourceId) {
        dataSourceConfigManager.getDataSourceById(dataSourceId);
        List<CollectionFieldVO> collectionFieldVOS = collectionFieldManager.getCollectionFieldByDataSourceId(dataSourceId);
        return ResponseVO.buildSuccess(collectionFieldVOS);
    }
}
