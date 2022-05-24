package com.cai.funtour.controller;

import cn.hutool.core.io.FileTypeUtil;
import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.config.UserConfig;
import com.cai.funtour.config.WebConfig;
import com.cai.funtour.po.User;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.BaseController;
import com.cai.funtour.tools.TraceId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:22
 * @description：用户公开接口
 */

@Api("用户公开接口")
@RestController
@RequestMapping("user")
@Slf4j
public class UserPublicController extends BaseController {
    @Reference
    private UserService user;


    @ApiOperation("登录接口")
    @PostMapping("login")
    public Result login(@ApiParam("参数account，password") @RequestBody Map<String, String> params) {
        log.debug(user == null ? "userService is Null" : "userService is Ready");
        Result result = user.login(params.get("account"), params.get("password"));
        Map map = JSONObject.parseObject((String) result.getData(), Map.class);
        User user = (User) map.get("user");
        map.put("token", UserConfig.getToken(user.getUserId()));
        return Result.toData(map);
    }

    @ApiOperation("登录接口")
    @PostMapping("/register")
    public Result register(@RequestBody User params) {
        Result result = user.register(params);
        Map map = JSONObject.parseObject((String) result.getData(), Map.class);
        User user = (User) map.get("user");
        map.put("token", UserConfig.getToken(user.getUserId()));

        return Result.toData(map);
    }

}
