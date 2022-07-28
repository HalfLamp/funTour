package com.cai.funtour.filter;

import com.cai.funtour.tools.Tools;
import com.cai.funtour.tools.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 15:44
 * @description：从http请求中获取traceId
 */
@Slf4j
@Component
@Order(0)
@WebFilter(filterName = "traceIdFilter", urlPatterns = "/")
public class HttpTraceInfoFilter implements Filter {
    public static final ThreadLocal<String> traceThreadLocal = new NamedThreadLocal<>("TraceId-ThreadLocal");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = request.getHeader(Tools.TRACE_ID);
        if (StringUtils.isBlank(traceId)){
            traceId = TraceId.getTraceId();
            log.debug("Generate TraceId：{}", traceId);
        }
        traceThreadLocal.set(traceId);
        MDC.put(Tools.TRACE_ID, traceId);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
