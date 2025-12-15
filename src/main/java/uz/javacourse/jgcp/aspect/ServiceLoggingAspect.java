package uz.javacourse.jgcp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Before("execution(* uz.javacourse.jgcp.service.*.*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.debug("Service method called: {}.{}", className, methodName);
    }

    @AfterReturning("execution(* uz.javacourse.jgcp.service.*.*(..))")
    public void logAfterReturningService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.debug("Service method completed: {}.{}", className, methodName);
    }

    @AfterThrowing(pointcut = "execution(* uz.javacourse.jgcp.service.*.*(..))", throwing = "ex")
    public void logAfterThrowingService(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("Service method failed: {}.{} with error: {}",
                className, methodName, ex.getMessage());
    }
}
