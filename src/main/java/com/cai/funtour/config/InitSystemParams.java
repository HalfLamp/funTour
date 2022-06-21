package com.cai.funtour.config;

import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.pojo.Result;
import com.qiniu.api.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/21 9:46
 * @description：系统参数初始化
 */
@Slf4j
@Component
public class InitSystemParams {

    @Reference
    private UserService user;

    @PostConstruct
    public void inits(){
        Result access_key = user.getSystemParams("QINIUYUN_ACCESS_KEY");
        Result secret_key = user.getSystemParams("QINIUYUN_SECRET_KEY");
        if (access_key.getCode() == 200 && secret_key.getCode() == 200){
            Config.ACCESS_KEY = access_key.getData().toString();
            Config.SECRET_KEY = secret_key.getData().toString();
        }
        else {
            log.error("未正确获取七牛云的access_key和secret_key");
        }
    }
}
