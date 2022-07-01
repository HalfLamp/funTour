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
import org.apache.commons.lang.StringUtils;
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

        // 添加redis缓存
        if (StringUtils.isNotBlank(map.get("token").toString())){
            String key = user.getUserId() + "_" + map.get("token").toString();
            this.user.setCache(key, JSONObject.toJSONString(user), String.valueOf(60*30));
        }

        return Result.toData(map);
    }

    @ApiOperation("注册接口")
    @PostMapping("/register")
    public Result register(@ApiParam("email/phone；password必填") @RequestBody User params) {

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

        // 添加redis缓存
        if (StringUtils.isNotBlank(map.get("token").toString())){
            String key = user.getUserId() + "_" + map.get("token").toString();
            this.user.setCache(key, JSONObject.toJSONString(user), String.valueOf(60*30));
        }

        return Result.toData(map);
    }

    @ApiOperation("更改用户信息")
    @PutMapping
    public Result changeUserMessage(@ApiParam("userId必填，其余的字段 传则更改，不传则不更改") @RequestBody User params) {
        Result result = user.changeUserMessage(params);
        if (result.getCode() != 200){
            return Result.error(result.getCode(), "更新失败");
        }else {
            return Result.ok();
        }
    }

    @ApiOperation("通过token获取用户信息")
    @GetMapping("/cache/{token}")
    public Result getCacheByToken(@ApiParam("token") @PathVariable String token) {
        return user.getCacheByToken(token);
    }

}
