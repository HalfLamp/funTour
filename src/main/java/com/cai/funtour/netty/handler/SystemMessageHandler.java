package com.cai.funtour.netty.handler;

import com.cai.funtour.model.NettyMessage;
import com.cai.funtour.netty.CacheUtil;
import com.cai.funtour.netty.NettyUtil;
import com.cai.funtour.tools.Tools;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/27 16:15
 * @description：系统消息处理
 */
@Slf4j
@Component
// 共享handler
@ChannelHandler.Sharable
public class SystemMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        NettyMessage message = NettyMessage.parseNettyMessage((msg).text());
        if (!message.isSystem()){
            ctx.fireChannelRead(msg.retain());
            return;
        }
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        // 清除缓存
        NettyUtil.removeCache(ctx);
        log.error("连接发生异常，已断开连接，channelId=" + ctx.channel().id().asLongText(), cause);
    }

}
