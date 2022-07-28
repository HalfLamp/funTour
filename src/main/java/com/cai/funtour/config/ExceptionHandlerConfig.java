package com.cai.funtour.config;

import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServlet;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 15:55
 * @description：全局错误统一处理类
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
        return ErrorResponse.getErrorResult(exception.getClass().getName());
    }
}
