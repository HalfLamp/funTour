package com.cai.funtour;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@SpringBootTest(classes = PublicConsumerApplication.class)
class PublicConsumerApplicationTests {

    @Test
    void contextLoads() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //keyPairGenerator.initialize(1024);
        //KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //
        //String publicBase64 = new BASE64Encoder().encode(publicKey.getEncoded());
        //String privateBase64 = new BASE64Encoder().encode(privateKey.getEncoded());
        //
        //System.out.println(publicBase64);
        //System.out.println(privateBase64);

        //byte[] bytes = new BASE64Decoder().decodeBuffer("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2dfvC8O5I4gQL/4wHrtkb0hUf8D9F8xZBvm9Y\n" +
        //        "fsfYzgsPRawTupmbLlvLNKP7NhLEE9rYGtpQ1REC65UnxNnhYLv7WKyDmvYNAODRrXCjUeYNj1ef\n" +
        //        "y5dAZZ0/U3YvafLrEI7DHtrR/LgfOyt62kEHq31TGqBGx1Tu6vDRVU2oSQIDAQAB");
        //X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(bytes);
        //PublicKey rsa = KeyFactory.getInstance("RSA").generatePublic(pkcs8EncodedKeySpec);
        //System.out.println(rsa);

        try {
            // 把私钥的Base64文本 转换为已编码的私钥bytes
            byte[] encPriKey = new BASE64Decoder().decodeBuffer("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALZ1+8Lw7kjiBAv/jAeu2RvSFR/w\n" +
                    "P0XzFkG+b1h+x9jOCw9FrBO6mZsuW8s0o/s2EsQT2tga2lDVEQLrlSfE2eFgu/tYrIOa9g0A4NGt\n" +
                    "cKNR5g2PV5/Ll0BlnT9Tdi9p8usQjsMe2tH8uB87K3raQQerfVMaoEbHVO7q8NFVTahJAgMBAAEC\n" +
                    "gYEAgVC9TjxTpXQKSjajmA5j8UPDlQ9AyOKwM4g0ghPYr9/YFsraWmE13LXOeQal8bUxN6aaw+7E\n" +
                    "K+XWWKt+2uC5Sb1Rix1aKTsRJwjWV7ELD+yiRhC0QIYq5byBt9RZPxYZGeBGWw+2zO6QAcpFIW8L\n" +
                    "92LzAJL1QTO2S2FYBpSJjMECQQD4xy6lkpYoAdRNRhowXTlvGuB2B2GYnOgMi1Q8PwaXadzschNe\n" +
                    "fxaryoh8GYcQqC65nVOtUTFGvbQai5/sKainAkEAu8H1cm7QJJa79BTwD/nsRMEySQ16/TXkbrjq\n" +
                    "hzQ6/fccHFm8aqHx9gbOxnCCiQ9rmhLy9clfhynMMDuz2s9VjwJAPUhTpVXGx3Qr6B0stl1AmK32\n" +
                    "PRz0XbPJNc3cojqkVQgu88/mFZEY9JifpnJjYycK24513mvxx4tTonZinrpkYQJATrRvfxiUh8Hn\n" +
                    "59kfCJNrRyb1PUo5VAtTRladeS+byvna/sdfBBP+qXMRGn3vOiUMpflrfzx6nt4WieF88ywWYQJB\n" +
                    "AKDt5cIYDn5aTNCDbwDGIjBjBcL8PNRJjHi2Hil1HkdGUWul7MtNGGaE7gQX3myxtFymWNrLqxu8\n" +
                    "7/s5B1266VE=");
            // 创建 已编码的私钥规格
            PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);
            // 获取指定算法的密钥工厂, 根据已编码的私钥规格, 生成私钥对象
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
