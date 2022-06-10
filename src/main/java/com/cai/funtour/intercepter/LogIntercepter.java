package com.cai.funtour.intercepter;

import com.cai.funtour.tools.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 15:43
 * @description：日志记录拦截器
 */
@Component
@Slf4j
public class LogIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getContextPath();
        String token = request.getHeader(Tools.TOKEN);
        log.info("请求路径： {}；用户token：{}", path, token);
        return true;
    }
}
