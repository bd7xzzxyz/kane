package xyz.bd7xzz.kane.collection;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.CollectionVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集数据处理器
 * @date 7/4/21 5:53 PM
 */
@Slf4j
public abstract class CollectionDataHandler {
    @Autowired
    private LocalCache localCache;

    /**
     * 提取json数据
     *
     * @param collectionVO 采集数据VO
     */
    public abstract void extract(CollectionVO collectionVO);

    /**
     * 从缓存中获取采集字段
     *
     * @param dataSourceId 数据源id
     * @return 采集字段vo
     */
    protected List<CollectionFieldVO> getCollectionFields(long dataSourceId) {
        try {
            return localCache.getCollectionFieldCache().get(dataSourceId);
        } catch (ExecutionException e) {
            log.error("get collection fields error when extract collection data", e);
            return Lists.newArrayListWithCapacity(0);
        }
    }
}
