package xyz.bd7xzz.kane.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 采集数据
 * @date 7/4/21 5:56 PM
 */
@Getter
@Builder
public class CollectionVO {
    private long dataSourceId;
    private int from;
    private long generationTime;
    private String jsonData;
}
