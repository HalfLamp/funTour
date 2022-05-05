package com.cai.funtour.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/5 15:09
 * @description：异常处理过滤器
 */

@Slf4j
@Activate(group = {"provider"}, order = 10)
@Component
public class ExceptionFilter extends org.apache.dubbo.rpc.filter.ExceptionFilter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        if (result.hasException()){
            Throwable exception = result.getException();
            log.error("执行结果异常，错误类型：{}；错误信息：{}", exception.getClass().getName(), exception.getMessage());
            log.error("错误栈信息: ", exception);
            return super.invoke(invoker, invocation);
        }
        return result;
    }
}
