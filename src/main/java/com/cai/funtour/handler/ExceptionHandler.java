package com.cai.funtour.handler;

import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.Tools;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/1 14:08
 * @description：全局异常处理类
 */
@Slf4j
@Order(-1)
@Component
public class ExceptionHandler implements WebExceptionHandler {

    private static Map<String, Result> errMap;
    static {
        errMap = new HashMap<String, Result>();
        errMap.put("ConnectException", Result.error(502, "服务连接出错"));
        errMap.put("TimeoutException", Result.error(505, "服务连接超时"));
        errMap.put("AnnotatedConnectException", Result.error(505, "服务连接超时"));
        errMap.put("ResourceAccessException", Result.error(505, "资源访问异常"));
    }

    public static Result getErrorResult(String simpleClassName){
        return errMap.getOrDefault(simpleClassName, Result.error("未知异常"));
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        MDC.put(Tools.TRACE_ID, exchange.getRequest().getHeaders().getFirst(Tools.TRACE_ID));

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        log.error("请求出错，错误类型：{};错误信息：{};错误原因：{}",ex.getClass().getSimpleName(), ex.getMessage(), ex.getCause());
        return responseError(exchange, ExceptionHandler.getErrorResult(ex.getClass().getSimpleName()).getCode(), ExceptionHandler.getErrorResult(ex.getClass().getSimpleName()).getErrMes());
    }

    /**
     * 拦截请求并响应错误信息
     *
     * @param exchange
     * @param code     请求错误编码
     * @param msg      请求错误信息
     * @return
     */
    private Mono<Void> responseError(ServerWebExchange exchange, Integer code, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        Result result = new Result();
        result.setCode(code);
        result.setErrMes(msg);
        byte[] bytes = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8");

        log.info("token校验失败，返回结果：{}", result);
        return response.writeWith(Mono.just(wrap));
    }
}
