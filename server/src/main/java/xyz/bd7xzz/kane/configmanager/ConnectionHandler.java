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
 * @description: 链接处理器
 * @date 7/4/21 12:12 PM
 */
public abstract class ConnectionHandler {
    @Autowired
    protected CollectionDataHandler collectionService;

    /**
     * 创建链接加入缓存
     */
    public static <T extends BasicDriverVO> void createConnection(int type, T driverVO) {
        ConnectionVO connection = createConnectionWithOutCache(type, driverVO);
        if (connection == null) return;
        LocalCache localCache = SpringContextUtil.getBean(LocalCache.class);
        localCache.getConnectionCache().put(driverVO.getId(), connection);
    }

    /**
     * 创建链接不加入缓存
     *
     * @param driverVO 驱动对象
     * @param type     数据源类型
     * @param <T>
     * @return 链接
     */
    public static <T extends BasicDriverVO> ConnectionVO createConnectionWithOutCache(int type, T driverVO) {
        String beanName = DataSourceDriverConstraint.getBeanName(type);
        ConnectionVO connection = SpringContextUtil.getBean(beanName, ConnectionHandler.class).createConnection(driverVO);
        if (null == connection) {
            return null;
        }
        connection.setVersion(driverVO.getVersion());
        return connection;
    }

    /**
     * 释放连接
     *
     * @param dataSourceId 数据源id
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
     * @param driverVO 数据源驱动
     */
    protected abstract <T> ConnectionVO createConnection(T driverVO);

    /**
     * 释放连接
     *
     * @param connectionVO 链接信息
     */
    protected abstract void releaseConnection(ConnectionVO connectionVO);

}
