package com.cai.funtour.netty.handler;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/27 15:27
 * @description：连接请求初始化
 */
@Slf4j
@Component
// 共享handler
@ChannelHandler.Sharable
public class ActiveRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        // 关闭连接
        String channelId = ctx.channel().id().asLongText();
        if (msg instanceof CloseWebSocketFrame){
            ctx.close();
            NettyUtil.removeCache(ctx);
            log.info("收到关闭连接请求，连接已关闭，缓存已清除。channleId={}", channelId);
            return;
        }
        // 处理ping请求
        if (msg instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            log.debug("收到ping请求，channleId={}", channelId);
            return;
        }
        // 文本消息处理
        if (!(msg instanceof TextWebSocketFrame)){
            NettyUtil.sendMessage(ctx.channel(), "仅支持文本数据");
            log.debug("不属于文本数据");
            return;
        }

        NettyMessage message = NettyMessage.parseNettyMessage(((TextWebSocketFrame) msg).text());
        if (!message.isActive()){
            ctx.fireChannelRead(msg.retain());
            return;
        }
        NettyUtil.setCache(ctx.channel(), message.getSender().getUserId(), channelId);
        log.info("客户端已完成初始化，绑定user和channelId。userId={}\tchannelId={}", message.getSender().getUserId(), channelId);
    }

    // 连接处理
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 存入本地缓存
        CacheUtil.put(ctx.channel().id().asLongText(), ctx.channel());
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("新的客户端已连接上：ip:{}\tport:{}\tchannelId:{}", channel.localAddress().getHostString(), channel.localAddress().getPort(), channel.id().asLongText());
    }

    // 客户端关闭连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 清除缓存
        NettyUtil.removeCache(ctx);
        log.info("客户端断开连接，已清除缓存，channelId：{}", ctx.channel().id().asLongText());
    }

}
