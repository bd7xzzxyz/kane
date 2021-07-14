package xyz.bd7xzz.kane.configmanager;

import xyz.bd7xzz.kane.constraint.ScheduleTypeConstraint;
import xyz.bd7xzz.kane.constraint.ServiceHandler;

/**
 * @author bd7xzz
 * @description: 调度服务
 * @date 2021/7/7 6:02 下午
 */
public interface ScheduledManager {



    /**
     * 启动定时调度
     *
     * @param serviceHandler 处理函数
     * @param param          业务参数
     * @param cron           cron表达式
     * @param type           任务类型
     * @param <P>
     * @return 处理结果
     */
    <P> void schedule(ServiceHandler serviceHandler, P param, String cron, ScheduleTypeConstraint type);

}
