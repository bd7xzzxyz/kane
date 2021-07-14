package xyz.bd7xzz.kane.configmanager.impl;

import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import xyz.bd7xzz.kane.component.SpringContextUtil;
import xyz.bd7xzz.kane.configmanager.ScheduledManager;
import xyz.bd7xzz.kane.constraint.ScheduleTypeConstraint;
import xyz.bd7xzz.kane.constraint.ServiceHandler;
import xyz.bd7xzz.kane.util.TimerUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

/**
 * @author bd7xzz
 * @description: 调度服务
 * @date 2021/7/7 6:02 下午
 */
@Service
public class ScheduledManagerImpl implements ScheduledManager {

    @PostConstruct
    public void init() {
        //TODO
    }

    @Override
    public <P> void schedule(ServiceHandler serviceHandler, P param, String cronExpression, ScheduleTypeConstraint type) {
        CronExpression cron = CronExpression.parse(cronExpression);
        LocalDateTime localDateTime = cron.next(LocalDateTime.now());
        TimerUtil.executeTask(TimerUtil.Task.newBuilder()
                .day(localDateTime.getDayOfMonth())
                .hour(localDateTime.getHour())
                .minute(localDateTime.getMinute())
                .second(localDateTime.getSecond())
                .once(false)
                .runnable(new TimerUtil.TaskExecutor() {
                    @Override
                    public void run() {
                        serviceHandler.doService(param);
                    }
                })
                .executor(SpringContextUtil.getBean(type.getExecutor(), Executor.class))
                .build());
    }
}
