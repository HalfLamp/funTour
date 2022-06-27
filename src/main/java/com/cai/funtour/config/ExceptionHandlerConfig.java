package com.cai.funtour.config;

import com.cai.funtour.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 17:26
 * @description：异常处理类
 */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception exception){
        log.error("请求发生错误,错误原因：{}\n错误类型：{}；错误信息: {}", exception.getCause(), exception.getClass().getName(), exception.getMessage());
        Arrays.stream(exception.getStackTrace()).limit(5).forEach(item -> {
            log.error("错误调用栈： {}", item.getClassName() + ":" + item.getMethodName() + ":" + item.getLineNumber());
        });
        return Result.error(500, "未知异常");
    }
}
