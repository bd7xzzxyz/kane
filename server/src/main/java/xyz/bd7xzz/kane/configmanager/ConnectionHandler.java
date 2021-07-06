package xyz.bd7xzz.kane.configmanager;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.bd7xzz.kane.cache.LocalCache;
import xyz.bd7xzz.kane.collection.CollectionDataHandler;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.constraint.DataSourceDriverConstraint;
import xyz.bd7xzz.kane.vo.ConnectionVO;
import xyz.bd7xzz.kane.vo.driver.BasicDriverVO;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 驱动处理器
 * @date 7/4/21 12:12 PM
 */
public abstract class ConnectionHandler {
    @Autowired
    protected CollectionDataHandler collectionService;

    /**
     * 创建连接
     */
    public static <T extends BasicDriverVO> void createConnection(int type, T driverVO) {
        String beanName = DataSourceDriverConstraint.getBeanName(type);
        ConnectionVO connection = SpringContextUtil.getBean(beanName, ConnectionHandler.class).createConnection(driverVO);
        if (null == connection) {
            return;
        }
        LocalCache localCache = SpringContextUtil.getBean(LocalCache.class);
        localCache.getConnectionCache().put(driverVO.getId(), connection);
    }

    /**
     * 释放连接
     *
     * @param dataSourceId
     */
    public static void destroyConnection(long dataSourceId) {
        LocalCache localCache = SpringContextUtil.getBean(LocalCache.class);
        ConnectionVO connectionVO = localCache.getConnectionCache().getIfPresent(dataSourceId);
        if (null == connectionVO) {
            return;
        }
        String beanName = DataSourceDriverConstraint.getBeanName(connectionVO.getType());
        SpringContextUtil.getBean(beanName, ConnectionHandler.class).releaseConnection(connectionVO);
        localCache.getConnectionCache().invalidate(dataSourceId);
    }

    /**
     * 创建连接
     *
     * @param driverVO
     */
    protected abstract <T> ConnectionVO createConnection(T driverVO);

    /**
     * 释放连接
     *
     * @param connectionVO
     */
    protected abstract void releaseConnection(ConnectionVO connectionVO);

}
