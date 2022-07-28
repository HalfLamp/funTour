package com.cai.funtour.controller;

import com.cai.funtour.exception.SingleServerException;
import com.cai.funtour.netty.server.WebSocketServer;
import com.cai.funtour.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/26 11:14
 * @description：netty服务器对外接口
 */

@Api("netty服务对外提供的操作接口")
@Slf4j
@RestController
@RequestMapping("netty/websocket")
public class NettyController {
    @Autowired
    private WebSocketServer webSocketServer;

    @ApiOperation("启动webSocket服务器")
    @PostMapping("start")
    public Result startServer(){
        try {
            webSocketServer.start();
        }catch (SingleServerException e){
            return new Result(202, e.getMessage());
        }
        return Result.ok();
    }

    @ApiOperation("销毁webSocket服务器")
    @PostMapping("destroy")
    public Result destroyServer(){
        try{
            webSocketServer.destroy();
        }catch (SingleServerException e){
            return new Result(202, e.getMessage());
        }
        return Result.ok();
    }
}
