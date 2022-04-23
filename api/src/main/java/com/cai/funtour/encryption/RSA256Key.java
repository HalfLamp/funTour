package com.cai.funtour.encryption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/22 16:33
 * @description：RS非对称加密，公私钥
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RSA256Key {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    //数字签名
    public static final String KEY_ALGORITHM = "RSA";
    //RSA密钥长度
    public static final int KEY_SIZE = 1024;
    private static volatile RSA256Key instance;

    public static RSA256Key getInstance() throws NoSuchAlgorithmException {
        if (instance == null){
            synchronized (RSA256Key.class){
                if (instance == null){
                    // 生成密钥对需要的随机数据源
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                    keyPairGenerator.initialize(KEY_SIZE);

                    // 生成密钥对
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();
                    RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
                    RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
                    RSA256Key rsa256Key = new RSA256Key(publicKey, privateKey);
                }
            }
        }
        return instance;
    }
}
