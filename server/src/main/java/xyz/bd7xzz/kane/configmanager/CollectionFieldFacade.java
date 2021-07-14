package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

import java.util.List;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集字段门面
 * @date 7/14/21 9:17 PM
 */
@Service
public class CollectionFieldFacade {

    private final CollectionFieldManager collectionFieldManager;

    @Autowired
    public CollectionFieldFacade(CollectionFieldManager collectionFieldManager) {
        this.collectionFieldManager = collectionFieldManager;
    }

    /**
     * 为数据源添加采集字段
     *
     * @param collectionFields 采集字段vo
     * @return ResponseVO 创建后的采集字段id列表
     */
    public ResponseVO addCollectionField(List<CollectionFieldVO> collectionFields) {
        return null;
    }

    /**
     * 更新采集字段
     *
     * @param collectionFieldVO 采集字段vo
     * @return ResponseVO对象
     */
    public ResponseVO updateCollectionField(CollectionFieldVO collectionFieldVO) {
        return null;
    }

    /**
     * 获取采集字段
     *
     * @param id 采集字段id
     * @return ResponseVO 采集字段
     */
    public ResponseVO getCollectionField(long id) {
        return null;
    }

    /**
     * 删除采集字段
     *
     * @param id 采集字段id
     * @return ResponseVO对象
     */
    public ResponseVO deleteCollectionField(long id) {
        return null;
    }

    /**
     * 根据数据源id获取所有采集字段
     *
     * @param dataSourceId 数据源id
     * @return 所有采集字段
     */
    public ResponseVO getCollectionFieldByDataSourceId(long dataSourceId) {
        return null;
    }
}
