package com.cai.funtour.tools;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.UrlSafeBase64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.qiniu.storage.Configuration;


import java.io.InputStream;
import java.util.UUID;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/21 10:32
 * @description：七牛云上传工具类
 */

@Component
public class QiniuUtil {
    @Value("${qiniuyun.access-key}")
    private String accessKey;
    @Value("${qiniuyun.secret-key}")
    private String secretKey;
    @Value("${qiniuyun.bucket}")
    private String bucket;
    @Value("${qiniuyun.domain}")
    private String domain;
    private String packageName = "images/";

    // 通过输入流上传到七牛云
    public String uploadToQiniu(InputStream inputStream, String fileName) {
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String prefix = "";
        int guid = 100;
        try {
            String s = auth.uploadToken(bucket);
            //实现文件上传
            Response response = uploadManager.put(inputStream, packageName + fileName, s, null, null);
            //解析上传成功结果
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回文件外链地址
            return domain + defaultPutRet.key;
        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 通过字节上传到七牛云
    public String uploadToQiniu(byte[] inputStream, String fileName) {
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String prefix = "";
        int guid = 100;
        try {
            String s = auth.uploadToken(bucket);
            //实现文件上传
            Response response = uploadManager.put(inputStream, packageName + fileName, s);
            //解析上传成功结果
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回文件外链地址
            return domain + defaultPutRet.key;
        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        }
    }
}
