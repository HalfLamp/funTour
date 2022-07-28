package com.cai.funtour.netty;

import com.alibaba.fastjson.JSON;
import com.cai.funtour.exception.SingleServerException;
import com.cai.funtour.model.NettyMessage;
import com.cai.funtour.pojo.BriefUserInfo;
import com.cai.funtour.tools.Tools;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/28 9:45
 * @description：netty操作工具类
 */
@Slf4j
@Component
public class NettyUtil {

    private static RedisTemplate<String, String> redisTemplate;
    private final static long EXPIRE_TIME = 7200;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        NettyUtil.redisTemplate = redisTemplate;
    }

    public static void sendMessage(Channel channel, String msg) {
        try {
            NettyMessage nettyMessage = JSON.parseObject(msg, NettyMessage.class);
            if (nettyMessage == null) {
                throw new SingleServerException("发送数据格式必须为NettyMessage");
            }
        } catch (Exception e) {
            log.error("数据类型转化错误，不满足NettyMessage格式", e);
        }
        sendMessage(channel, new TextWebSocketFrame(msg));
    }

    public static void sendMessage(Channel channel, TextWebSocketFrame msg) {
        channel.writeAndFlush(msg);
    }

    public static void sendSystemMessage(Channel channel, String msg) {
        NettyMessage systemMessage = new NettyMessage();
        systemMessage.setFlag("SYSTEM");
        systemMessage.setMessage(msg);
        TextWebSocketFrame webSocketFrame = systemMessage.toFrame();
        sendMessage(channel, webSocketFrame);
    }

    public static void removeCache(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asLongText();
        CacheUtil.remove(channelId);
        // 通过channelId获取userId
        String userId = redisTemplate.opsForValue().get(Tools.NETTY_CHANNEL + channelId);
        redisTemplate.delete(Tools.NETTY_CHANNEL + channelId);
        redisTemplate.delete(Tools.NETTY_CHANNEL + userId);
    }

    public static void setCache(Channel channel, String userId, String channelId) {
        // 绑定userId和channelId到redis缓存
        String userKey = Tools.NETTY_CHANNEL + userId;
        String channelKey = Tools.NETTY_CHANNEL + channelId;
        // 双向绑定
        String value = redisTemplate.opsForValue().get(channelKey);
        if (StringUtils.isNotBlank(value)) {
            sendSystemMessage(channel, "该通道已经绑定一个用户，不可重复绑定");
            log.warn("该通道已经绑定一个用户，不可重复绑定");
            return;
        }
        redisTemplate.opsForValue().setIfAbsent(userKey, channelId);
        redisTemplate.opsForValue().setIfAbsent(channelKey, userId);
        redisTemplate.expire(userKey, EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.expire(channelKey, EXPIRE_TIME, TimeUnit.SECONDS);
    }

}
