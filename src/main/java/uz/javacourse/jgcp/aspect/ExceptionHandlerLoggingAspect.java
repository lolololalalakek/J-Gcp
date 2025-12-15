package uz.javacourse.jgcp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import uz.javacourse.jgcp.exception.BaseException;

@Slf4j
@Aspect
@Component
public class ExceptionHandlerLoggingAspect {

    @AfterThrowing(pointcut = "execution(* uz.javacourse.jgcp.handler.*.*(..))", throwing = "ex")
    public void logAfterThrowingInHandler(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        if (ex instanceof BaseException) {
            BaseException baseException = (BaseException) ex;
            log.warn("Handled business exception in {}: {}", methodName, baseException.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            log.warn("Validation failed in {}", methodName);
        } else {
            log.error("Unexpected error occurred in {}", methodName, ex);
        }
    }
}
