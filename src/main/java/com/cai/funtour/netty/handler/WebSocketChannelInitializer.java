package com.cai.funtour.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cai.funtour.model.NettyMessage;
import com.cai.funtour.netty.CacheUtil;
import com.cai.funtour.tools.Tools;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/26 14:38
 * @description：webSocket处理器初始化
 */
@Slf4j
@Component
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> implements MessageListener {
    public static Map<String, Channel> cachedChannel = new HashMap<>();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ActiveRequestHandler activeRequestHandler;
    @Autowired
    private HistoryRequestHandler historyRequestHandler;
    @Autowired
    private MessageForwardingHandler messageForwardingHandler;
    @Autowired
    private SystemMessageHandler systemMessageHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 存入本地缓存
        CacheUtil.put(ch.id().asLongText(), ch);

        // 添加日志处理器
        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        // 添加http编解码器
        pipeline.addLast("http-codec", new HttpServerCodec());
        // http请求聚合器
        pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
        // 升级webSocket协议(以帧传输数据（继承自WebSocketFrame）)
        pipeline.addLast("websocket", new WebSocketServerProtocolHandler("/websocket"));
        // 业务handler
        pipeline.addLast("message-active", activeRequestHandler);
        pipeline.addLast("message-history", historyRequestHandler);
        pipeline.addLast("message-forwarding", messageForwardingHandler);
        pipeline.addLast("message-system", systemMessageHandler);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        NettyMessage nettyMessage = JSON.parseObject(message.getBody(), NettyMessage.class);
        // 获取接收者的channelId
        String channelId = redisTemplate.opsForHash().get(Tools.NETTY_CHANNEL, nettyMessage.getReceiver().getUserId() + "*").toString();
        if(StringUtils.isBlank(channelId)){
            return;
        }
        // 判断channelId是否在本地
        Channel channel = CacheUtil.getChanel(channelId);
        if (channel == null){
            return;
        }
        // 转发消息
        channel.writeAndFlush(nettyMessage.toFrame());
        log.info("转发消息：{}", nettyMessage.toJson());
    }
}
