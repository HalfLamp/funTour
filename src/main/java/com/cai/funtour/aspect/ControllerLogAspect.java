package com.cai.funtour.aspect;

import com.cai.funtour.pojo.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 15:57
 * @description：controller层日志切面
 */

@Aspect
@Component
public class ControllerLogAspect {

    @Around("execution(com.cai.funtour.pojo.Result com.cai.funtour.controller..*.*(..))")
    public Object addControllerLog(ProceedingJoinPoint pjp) throws Throwable {
        Result result = null;
        Class<?> targetClass = pjp.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(targetClass);

        Object[] args = pjp.getArgs();
        logger.info("请求参数： {}", Arrays.toString(args));

        result = (Result) pjp.proceed();
        logger.info("返回结果： {}", result == null ? "null" : result);
        return result;
    }
}

