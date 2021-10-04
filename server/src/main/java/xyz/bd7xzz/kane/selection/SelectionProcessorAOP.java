package xyz.bd7xzz.kane.selection;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import xyz.bd7xzz.kane.vo.TaskContextVO;

/**
 * @author baodi1
 * @description: 筛选流程aop
 * @date 2021/10/4 6:43 下午
 */
@Aspect
@Component
public class SelectionProcessorAOP {

    @Pointcut("execution(* xyz.bd7xzz.kane.selection.SelectionProcessor.doProcess(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (ArrayUtils.isEmpty(joinPoint.getArgs())) {
            return result;
        }
        TaskContextVO taskContextVO = null;
        for (Object arg : joinPoint.getArgs()) {
            if (null == arg) {
                continue;
            }
            if (arg instanceof TaskContextVO) {
                taskContextVO = (TaskContextVO) arg;
                break;
            }
        }
        if (null == taskContextVO) {
            return result;
        }
        if (taskContextVO.getSignalFlag() != null) {
            taskContextVO.setSignalFlag(null);
            return SelectionSignalRegister.checkAndExecute(taskContextVO);
        }
        return result;
    }
}
