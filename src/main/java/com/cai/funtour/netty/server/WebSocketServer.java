package com.cai.funtour.netty.server;

import com.cai.funtour.exception.SingleServerException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/26 11:19
 * @description：websocket服务端初始化
 */

@Slf4j
@Component
public class WebSocketServer {

    private final static EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static Channel channel = null;

    @Value("${netty.port}")
    public Integer port;

    @Autowired
    private ChannelInitializer<SocketChannel> websocketChannelInitializer;

    // 默认启动websocket服务器
    @PostConstruct
    private void init(){
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //全连接队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //尽可能发送大的数据块
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 两小时内没有数据交换则探测是否存活
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(websocketChannelInitializer);

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 使用接口对通道进行关闭
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            log.error("启动WebSocket失败", e);
        }
    }

    // 关闭webSocket服务器
    public void destroy(){
        if (channel == null){
            throw new SingleServerException("不存在websocket通道");
        }
        channel.close();
        channel = null;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    // 外部启动websocket服务器
    public void start(){
        if (channel.isActive()){
            log.error("已存在websocket服务实例，不可重复创建");
            throw new SingleServerException("已存在websocket通道");
        }

        init();
    }

}
