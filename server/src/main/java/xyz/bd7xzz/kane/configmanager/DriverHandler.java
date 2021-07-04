package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 驱动处理器
 * @date 7/4/21 12:12 PM
 */
public abstract class DriverHandler {
    @Autowired
    protected CollectionDataHandler collectionService;
    @Autowired
    protected LocalCache localCache;

    /**
     * 处理驱动
     */
    public static <T extends BasicDriverVO> void handle(int type, T driverVO) {
        String beanName = DataSourceDriverConstraint.getBeanName(type);
        SpringContextUtil.getBean(beanName, DriverHandler.class).handle(driverVO);
    }


    /**
     * 驱动处理
     *
     * @param driverVO
     */
    public abstract <T> void handle(T driverVO);

}
