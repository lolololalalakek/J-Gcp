package uz.javacourse.jgcp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Before("execution(* uz.javacourse.jgcp.controller.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Controller method called: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning("execution(* uz.javacourse.jgcp.controller.*.*(..))")
    public void logAfterReturningController(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Controller method completed: {}.{}", className, methodName);
    }

    @AfterThrowing(pointcut = "execution(* uz.javacourse.jgcp.controller.*.*(..))", throwing = "ex")
    public void logAfterThrowingController(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("Controller method failed: {}.{} with error: {}",
                className, methodName, ex.getMessage());
    }
}
