package com.cai.funtour.controller;

import com.cai.funtour.api.pub.User;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 16:22
 * @description：用户公开接口
 */

@Api("用户公开接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserPublicController extends BaseController {
    @Reference
    User user;

    @ApiOperation("登录接口，参数account，password")
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        Result result = user.login(params.get("account"), params.get("password"));
        return result;
    }

    @ApiOperation("登录接口")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        Result result = user.register(user);
        return result;
    }
}
