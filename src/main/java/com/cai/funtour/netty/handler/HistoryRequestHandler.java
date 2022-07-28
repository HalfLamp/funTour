package com.cai.funtour.netty.handler;

import com.cai.funtour.model.NettyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/27 16:13
 * @description：获取聊天历史记录
 */
@Slf4j
@Component
// 共享handler
@ChannelHandler.Sharable
public class HistoryRequestHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        NettyMessage message = NettyMessage.parseNettyMessage((msg).text());
        if (!message.isHistory()){
            ctx.fireChannelRead(msg.retain());
            return;
        }
    }
}
