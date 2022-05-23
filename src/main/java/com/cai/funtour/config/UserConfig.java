package com.cai.funtour.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cai.funtour.encryption.RSA256Key;
import com.cai.funtour.po.User;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/9 10:37
 * @description：用户token配置
 */
@Slf4j
public class UserConfig {
    public static String getToken(String userId) {
        try {
            String token = JWT.create()
                    .withAudience(userId)
                    .withIssuedAt(new Date())
                    .sign(Algorithm.RSA256(RSA256Key.getInstance().getPrivateKey()));
            return token;
        } catch (NoSuchAlgorithmException e) {
            log.error("RSA私钥生成错误", e);
            return null;
        }
    }

    public static String getUserId(String token){
        String userId = JWT.decode(token).getAudience().get(0);
        return userId;
    }
}
