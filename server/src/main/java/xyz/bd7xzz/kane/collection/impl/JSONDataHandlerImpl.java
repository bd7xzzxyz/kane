package xyz.bd7xzz.kane.collection.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.vo.CollectionVO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: json数据处理器
 * @date 7/4/21 5:53 PM
 */
@Service
public class JSONDataHandlerImpl implements CollectionDataHandler {

    @Override
    public void extract(CollectionVO collectionVO) {
        if (StringUtils.isEmpty(collectionVO.getJsonData())) {
            return;
        }

        //TODO jsonPath
    }
}
