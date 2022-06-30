package com.cai.funtour.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cai.funtour.encryption.RSA256Key;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.Tools;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/22 14:23
 * @description：校验用户token
 */
@Component
@Slf4j
public class AccessFilter implements GlobalFilter, Ordered {
    private final String PUBLIC_URL = "/public";
    private final String ALLOW_URL = "/pub";
    private final String SWAGGER_URL = "/swagger-ui.html";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(Tools.TOKEN);
        String path = exchange.getRequest().getPath().toString();
        log.info("请求路径：{}；请求token：{}", path, token);

        // 公共请求，不检查token
        if (path.contains(PUBLIC_URL) || path.contains(SWAGGER_URL) || path.contains(ALLOW_URL)) {
            return chain.filter(exchange);
        }
        // 不含token
        if (StringUtils.isBlank(token)) {
            return responseError(exchange, 401, "不含token");
        }

        // 从redis验证并刷新token有效期
        try {
            String key = "funTour:token:" + token;
            String url = "http://czytgc.com:8771/public/user/cache/" + key;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result> response = restTemplate.exchange(url, HttpMethod.GET, null, Result.class);
            Result cache = response.getBody();
            if (cache == null) {
                return responseError(exchange, 401, "token过期");
            }
        }catch (Exception e){
            return responseError(exchange, 503, "验证token出错");
        }

        // token校验
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(RSA256Key.getInstance().getPublicKey())).withIssuer(Tools.JWT_IISUSER).build();
            DecodedJWT decodedJWT = verifier.verify(token);
        } catch (NoSuchAlgorithmException e) {
            log.error("RSA公私钥生成错误", e);
            return responseError(exchange, 500, "系统错误");
        } catch (JWTVerificationException e) {
            return responseError(exchange, 401, "token校验失败");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
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
