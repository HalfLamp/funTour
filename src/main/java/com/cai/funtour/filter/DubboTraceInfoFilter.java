package com.cai.funtour.filter;

import com.cai.funtour.tools.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.stereotype.Component;


/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 15:49
 * @description：为Dubbo请求添加traceId
 */
@Slf4j
@Component
@Activate
public class DubboTraceInfoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = HttpTraceInfoFilter.traceThreadLocal.get();
        if (traceId != null){
            RpcContext.getContext().setAttachment(Tools.TRACE_ID, traceId);
            log.info(RpcContext.getContext().toString());
        }
        return invoker.invoke(invocation);
    }
}
