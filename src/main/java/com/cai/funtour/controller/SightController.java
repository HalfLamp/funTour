package com.cai.funtour.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.api.pub.SightService;
import com.cai.funtour.api.pub.UserService;
import com.cai.funtour.config.KafkaProducer;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    @Autowired
    KafkaProducer kafkaProducer;


    @ApiOperation("景点查询接口")
    @GetMapping("pub/search")
    public Result search(@ApiParam("关键词") @RequestParam(value = "key", required = false) String key,
                         @ApiParam("类型列表") @RequestParam(value = "type", required = false) String[] type,
                         @ApiParam("地区列表") @RequestParam(value = "regionCode", required = false) String[] regionCode,
                         @ApiParam("页码") @RequestParam(value = "page") Integer page,
                         @ApiParam("每页数量") @RequestParam(value = "size") Integer size,
                         @RequestHeader(value = "token", required = false) String headerToken) {
        String userId = "";
        Result token = userService.getCacheByToken(headerToken == null ? "" : headerToken);
        String data = token.getCode() == 200 ? token.getData().toString() : null;
        if (data != null) {
            Map userInfo = JSONObject.parseObject(data, Map.class);
            userId = userInfo.get("userId").toString();
        }
        // 没有添加限制条件
        if (key == null && type == null && regionCode == null) {
            // 根据用户推荐个性化的景点列表
            return sightService.getSightListByUser(userId, page, size);
        } else {
            // 根据条件查询景点列表
            if (key == null) {
                key = "";
            }
            if (type == null) {
                type = new String[]{};
            }
            if (regionCode == null) {
                regionCode = new String[]{};
            }
            return sightService.getSightList(userId, key, type, regionCode, page, size);
        }
    }

    @ApiOperation("景点详细信息")
    @GetMapping("pub/getSightById/{sightId}")
    public Result getSightInfo(@ApiParam("景点id") @PathVariable("sightId") String sightId,
                               @RequestHeader(value = "userId", required = false) String userId) {
        HashMap<String, String> msg = new HashMap<>(3);
        msg.put("sightId", sightId);
        msg.put("weight", "1");
        // 投递kafka消息：热门景点
        //kafkaProducer.send(Tools.KAFKA_TOPIC_HOTSIGHT, JSON.toJSONString(msg), null);
        /// 有userId则投递kafka消息：用户偏好景点
        if (StringUtils.isNotBlank(userId)) {
            msg.put("userId", userId);
            kafkaProducer.send(Tools.KAFKA_TOPIC_PREFERENCE, JSON.toJSONString(msg), null);
        }


        return sightService.getSightInfo(sightId);
    }

    @ApiOperation(value = "获取相似景点", notes = "根据景点ID查询其他类似的景点")
    @GetMapping("pub/getSimilarSights/{sightId}")
    public Result getSimilarSightsById(@ApiParam("景点id") @PathVariable("sightId") String sightId,
                                       @ApiParam("期望的相似类型的数量") @RequestParam("typeSize") int typeSize,
                                       @ApiParam("期望的相似地点的数量") @RequestParam("regionSize") int regionSize) {
        return sightService.similarSights(sightId, typeSize, regionSize);
    }

    @ApiOperation(value = "景点收藏")
    @GetMapping("collect/{sightId}")
    public Result sightCollection(@ApiParam("景点Id") @PathVariable("sightId") String sightId,
                                  @ApiParam("用户Id") @RequestHeader("userId") String userId) {
        // 投递kafka热门景点消息
        HashMap<String, String> message = new HashMap<>(2);
        message.put("sightId", sightId);
        message.put("weight", "3");
        kafkaProducer.send(Tools.KAFKA_TOPIC_HOTSIGHT, JSON.toJSONString(message), null);

        return sightService.collectSight(sightId, userId);
    }

    @ApiOperation(value = "添加足迹", notes = "添加景点到用户足迹")
    @GetMapping("footMark/{sightId}")
    public Result footMark(@ApiParam("景点Id") @PathVariable("sightId") String sightId,
                           @ApiParam("用户Id") @RequestHeader("userId") String userId) {
        return sightService.addToFootMark(sightId, userId);
    }

}
