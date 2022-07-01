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
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
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

    @Autowired
    RestTemplate restTemplate;

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

        // token校验
        //try {
        //    // 获取publicKey
        //    String url = "http://czytgc.com:8771/public/system/getPublicKey";
        //    ResponseEntity<Result> response = restTemplate.exchange(url, HttpMethod.GET, null, Result.class);
        //    Result result = response.getBody();
        //    String publicString = result.getData().toString();
        //    PublicKey publicKey = parsePublicKey(publicString);
        //    PrivateKey privateKey = parsePublicKey()
        //    String userId = JWT.decode(token).getAudience().get(0);
        //    JWTVerifier verifier = JWT.require(Algorithm.RSA256((RSAPublicKey)publicKey, (RSAPrivateKey) privateKey).withIssuer(Tools.JWT_IISUSER).build();
        //    DecodedJWT decodedJWT = verifier.verify(token);
        //    // 把userId放入请求头
        //    exchange.getRequest().mutate().header("userId", userId);
        //} catch (NoSuchAlgorithmException e) {
        //    log.error("RSA公私钥生成错误", e);
        //    return responseError(exchange, 500, "系统错误");
        //} catch (JWTVerificationException e) {
        //    return responseError(exchange, 401, "token校验失败");
        //} catch (Exception e){
        //    return responseError(exchange, 502, "token验签出错");
        //}

        // 从redis验证并刷新token有效期
        try {
            String url = "http://czytgc.com:8771/public/user/cache/" + token;
            ResponseEntity<Result> response = restTemplate.exchange(url, HttpMethod.GET, null, Result.class);
            Result cache = response.getBody();
            if (cache.getCode() != 200) {
                return responseError(exchange, 401, "token过期");
            }
        }catch (Exception e){
            return responseError(exchange, 503, "验证token出错");
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

    public PublicKey parsePublicKey(String buffer) {
        try {
            // 把公钥的Base64文本 转换为已编码的公钥bytes
            byte[] encPubKey = new BASE64Decoder().decodeBuffer(buffer);
            // 创建已编码的公钥规格
            X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
            // 获取指定算法的密钥工厂, 根据已编码的公钥规格, 生成公钥对象
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
            return pubKey;
        } catch (IOException e) {
            log.error("转换公钥bytes出错", e);
        } catch (InvalidKeySpecException e) {
            log.error("获取KeyFactory出错",e);
        } catch (NoSuchAlgorithmException e) {
            log.error("生成公钥失败", e);
        }
        return null;
    }
}
