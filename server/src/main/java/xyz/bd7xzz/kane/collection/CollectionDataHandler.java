package xyz.bd7xzz.kane.collection;

import xyz.bd7xzz.kane.vo.CollectionVO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集数据处理器
 * @date 7/4/21 5:53 PM
 */
public interface CollectionDataHandler {

    /**
     * 提取json数据
     *
     * @param collectionVO 采集数据VO
     */
    void extract(CollectionVO collectionVO);
}
