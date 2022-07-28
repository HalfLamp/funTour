package com.cai.funtour.netty.handler;

import com.cai.funtour.config.RedisConfig;
import com.cai.funtour.model.NettyMessage;
import com.cai.funtour.netty.CacheUtil;
import com.cai.funtour.netty.NettyUtil;
import com.cai.funtour.tools.Tools;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/26 16:28
 * @description：消息转发处理器
 */
@Slf4j
@Component
// 共享handler
@ChannelHandler.Sharable
public class MessageForwardingHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        NettyMessage message = NettyMessage.parseNettyMessage((msg).text());
        if (!message.isForward()){
            ctx.fireChannelRead(msg.retain());
            return;
        }
        String channelId = redisTemplate.opsForValue().get(Tools.NETTY_CHANNEL + message.getReceiver().getUserId());
        // 本地不存在该channel则向redis发布消息
        if (!CacheUtil.contains(channelId)){
            redisTemplate.convertAndSend(RedisConfig.NETTY_CHANNEL_TOPIC, message.toJson());
            ctx.fireChannelRead(msg.retain());
            return;
        }
        // 该channel在本地则转发消息
        Channel receiverChanel = CacheUtil.getChanel(channelId);
        NettyUtil.sendMessage(receiverChanel, msg);
    }


}
