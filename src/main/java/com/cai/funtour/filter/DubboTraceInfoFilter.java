package com.cai.funtour.filter;

import com.cai.funtour.tools.Tools;
import com.cai.funtour.tools.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:09
 * @description：将请求头中的traceId取出并加入MDC
 */
@Slf4j
@Activate(group = {"provider"},order = 0)
@Component
public class DubboTraceInfoFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(Tools.TRACE_ID);
        if (StringUtils.isBlank(traceId)){
            traceId = TraceId.getTraceId();
            log.debug("Generate TraceId：{}", traceId);
        }
        MDC.put(Tools.TRACE_ID, traceId);
        return invoker.invoke(invocation);
    }
}
