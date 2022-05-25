package com.cai.funtour.config;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/5/9 10:37
 * @description：用户token配置
 */
@Slf4j
@Component
public class UserConfig {
    @Reference
    private UserService userService;

    public String getToken(String userId) throws Exception {
        Result result = userService.getSystemParams("RSA256_PRIVATE_KEY");
        String buffer = (String) result.getData();
        PrivateKey privateKey = null;
        privateKey = parsePrivateKey(buffer);
        if(privateKey == null){
            log.error("未正确获取私钥");
            throw new Exception("未正确获取私钥");
        }

        String token = JWT.create()
                .withAudience(userId)
                .withIssuedAt(new Date())
                .sign(Algorithm.RSA256(null, (RSAPrivateKey)privateKey));
        return token;
    }

    public String getUserId(String token) {
        String userId = JWT.decode(token).getAudience().get(0);
        return userId;
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

    public PrivateKey parsePrivateKey(String buffer) {
        try {
            // 把私钥的Base64文本 转换为已编码的私钥bytes
            byte[] encPriKey = new BASE64Decoder().decodeBuffer(buffer);
            // 创建 已编码的私钥规格
            PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);
            // 获取指定算法的密钥工厂, 根据已编码的私钥规格, 生成私钥对象
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
            return priKey;
        } catch (IOException e) {
            log.error("转换私钥bytes出错", e);
        } catch (InvalidKeySpecException e) {
            log.error("获取KeyFactory出错",e);
        } catch (NoSuchAlgorithmException e) {
            log.error("生成私钥失败", e);
        }
        return null;
    }
}
