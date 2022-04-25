package com.cai.funtour.filter;

import com.cai.funtour.tools.Tools;
import com.cai.funtour.tools.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/22 14:29
 * @description：为所有请求添加traceId
 */
@Component
@Slf4j
public class TraceIdFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 日志traceId处理
        String traceId = TraceId.getTraceId();
        log.debug("Generate TraceId: {}", traceId);
        mutate.header(Tools.TRACE_ID, traceId);
        MDC.put(Tools.TRACE_ID, traceId);

        ServerHttpRequest build = mutate.build();
        exchange.mutate().request(build).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
