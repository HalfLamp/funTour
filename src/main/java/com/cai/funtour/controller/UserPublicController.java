package com.cai.funtour.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.config.UserConfig;
import com.cai.funtour.filter.HttpTraceInfoFilter;
import com.cai.funtour.po.User;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.BaseController;
import com.cai.funtour.tools.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    UserConfig userConfig;


    @ApiOperation("登录接口")
    @PostMapping("login")
    public Result login(@ApiParam("参数account，password") @RequestBody Map<String, String> params) {

        log.info(user == null ? "userService is Null" : "userService is Ready");
        Result result = user.login(params.get("account"), params.get("password"));
        Map map = JSONObject.parseObject((String) result.getData(), Map.class);
        // 为空代表没有取得用户数据。
        if (map == null || map.isEmpty()){
            return result;
        }
        User user = JSONObject.parseObject(map.get("user").toString(), User.class, Feature.IgnoreNotMatch);

        // 生成token
        try {
            map.put("token", userConfig.getToken(user.getUserId()));
        } catch (Exception e) {
            log.error("未正确生成token", e);
            return Result.error(407, "未生成token");
        }

        return Result.toData(map);
    }

    @ApiOperation("登录接口")
    @PostMapping("/register")
    public Result register(@RequestBody User params) {

        Result result = user.register(params);
        Map map = JSONObject.parseObject((String) result.getData(), Map.class);
        // 为空代表没有取得用户数据。
        if (map == null || map.isEmpty()){
            return result;
        }
        User user = JSONObject.parseObject(map.get("user").toString(), User.class, Feature.IgnoreNotMatch);

        // 生成token
        try {
            map.put("token", userConfig.getToken(user.getUserId()));
        } catch (Exception e) {
            log.error("未正确生成token");
            return Result.error(407, "未生成token");
        }

        return Result.toData(map);
    }

    @ApiOperation("更改用户信息")
    @PutMapping
    public Result changeUserMessage(@RequestBody User params) {
        Result result = user.changeUserMessage(params);
        if (result.getCode() != 200){
            return Result.error(result.getCode(), "更新失败");
        }else {
            return Result.ok();
        }
    }

}
