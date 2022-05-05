package com.cai.funtour.aspect;

import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.pojo.Result;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.exceptions.IbatisException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/3 11:08
 * @description：service层日志记录切面
 */
@Component
@Aspect
public class LogAspect {

    @Around("execution(* com.cai.funtour.service..*.*(..))")
    public Object addAroundLog(ProceedingJoinPoint pjp){
        Object result = null;
        Class<?> targetClass = pjp.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(targetClass);

        Object[] args = pjp.getArgs();
        logger.info("请求参数：{}", JSONObject.toJSONString(args));

        try {
            result = pjp.proceed();
        } catch (IbatisException e) {
            logger.error("执行结果异常，错误类型：{}；错误信息：{}", e.getClass().getName(), e.getMessage());
            logger.error("错误栈信息: ", e);
            result = Result.error(501, "Mybatis错误");
        } catch (Throwable e) {
            logger.error("执行结果异常，错误类型：{}；错误信息：{}", e.getClass().getName(), e.getMessage());
            logger.error("错误栈信息: ", e);
            result = Result.error(500, e.getMessage());
        } finally {
            logger.info("返回结果：{}", result == null ? "null" : result);
            return result;
        }
    }

}
