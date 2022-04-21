package com.cai.funtour.filter;

import com.cai.funtour.tools.Tools;
import com.cai.funtour.tools.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 15:01
 * @description：为dubbo请求添加traceid
 */

@Slf4j
@Activate(group = {"consumer"})
@Component
public class DubboTraceInfoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = HttpTraceInfoFilter.traceThreadLocal.get();
        if (traceId != null){
            RpcContext.getContext().setAttachment(Tools.TRACE_ID, traceId);
        }
        return invoker.invoke(invocation);
    }
}
