package com.cai.funtour.aspect;

import com.cai.funtour.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:51
 * @description：controller层日志切面
 */
@Aspect
@Component
public class ControllerLogAspect {

    @Around("execution(com.cai.funtour.pojo.Result com.cai.funtour.controller..*.*(..))")
    public Object addControllerLog(ProceedingJoinPoint pjp) {
        Result result = null;

        Class<?> targetClass = pjp.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(targetClass);

        Object[] args = pjp.getArgs();
        logger.info("请求参数： {}", Arrays.toString(args));

        try {
            result = (Result)pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }finally {
            logger.info("返回结果： {}", result == null ? "null" : result);
            return result;
        }
    }

}
