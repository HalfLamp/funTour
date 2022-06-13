package com.cai.funtour.controller;

import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.api.pub.SightService;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 16:56
 * @description：景点Controller层
 */
@Api("景点查询接口")
@Slf4j
@RestController
@RequestMapping
public class SightController {
    @Reference
    SightService sightService;
    @Reference
    UserService userService;


    @ApiOperation("景点查询接口")
    @GetMapping("search")
    public Result search(@ApiParam("关键词") @RequestParam(value = "key", required = false) String key,
                         @ApiParam("类型列表") @RequestParam(value = "type", required = false) List<String> type,
                         @ApiParam("地区列表") @RequestParam(value = "regionCode", required = false) List<String> regionCode,
                         @ApiParam("页码") @RequestParam(value = "page") Integer page,
                         @ApiParam("每页数量") @RequestParam(value = "size") Integer size) {
        // TODO 根据token从缓存获取userid，通过userId获取相关用户的景点列表
        String userId = "";
        Result token = userService.getCacheByToken("token");
        String data = token.getCode() == 200 ? token.getData().toString() : null;
        if (data != null) {
            Map userInfo = JSONObject.parseObject(data, Map.class);
            userId = userInfo.get("userId").toString();
        }else {
            return Result.error(token.getCode(), "token失效");
        }
        // 没有添加限制条件
        if (key == null && type == null && regionCode == null) {
            // 根据用户推荐个性化的景点列表
            return sightService.getSightListByUser(userId,page,size);
        } else {
            // 根据条件查询景点列表
            return sightService.getSightList(userId,key,type,regionCode,page,size);
        }
    }
}
