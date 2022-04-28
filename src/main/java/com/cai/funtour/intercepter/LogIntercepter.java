package com.cai.funtour.intercepter;

import com.cai.funtour.tools.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 17:04
 * @description：日志记录拦截器
 */
@Slf4j
@Component
public class LogIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getContextPath();
        String token = request.getHeader(Tools.TOKEN);
        log.info("请求路径： {}；用户token：{}", path, token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
