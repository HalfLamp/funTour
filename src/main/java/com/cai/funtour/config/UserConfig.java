package com.cai.funtour.config;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.encryption.RSA256Key;
import com.cai.funtour.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/9 10:37
 * @description：用户token配置
 */
@Slf4j
public class UserConfig {
    @Reference
    private static UserService userService;

    public static String getToken(String userId) {
        Result result = userService.getSystemParams("RSA256_PRIVATE_KEY");
        String data = (String) result.getData();
        RSAPrivateKey rsaPrivateKey = JSONObject.parseObject(data, RSAPrivateKey.class);
        String token = JWT.create()
                .withAudience(userId)
                .withIssuedAt(new Date())
                .sign((Algorithm) rsaPrivateKey);
        return token;
    }

    public static String getUserId(String token){
        String userId = JWT.decode(token).getAudience().get(0);
        return userId;
    }
}
