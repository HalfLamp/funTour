package com.cai.funtour.model;

import com.alibaba.fastjson.JSON;
import com.cai.funtour.pojo.BriefUserInfo;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/27 10:52
 * @description：websocket数据传输实体类
 */

@Data
@NoArgsConstructor
public class NettyMessage {
    private static final String ACTIVE = "ACTIVE";
    private static final String HISTORY = "HISTORY";
    private static final String FORWARD = "FORWARD";
    private static final String SYSTEM = "SYSTEM";

    private BriefUserInfo sender;
    private BriefUserInfo receiver;
    private String message;
    private long timestamp;
    // ACTIVE（激活连接）；HISTORY（获取历史记录）；SEND（发送消息）；SYSTEM（系统消息）
    private String flag;

    public TextWebSocketFrame toFrame(){
        return new TextWebSocketFrame(toJson());
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public static NettyMessage parseNettyMessage(String msg){
        return JSON.parseObject(msg, NettyMessage.class);
    }

    public boolean isActive(){
        return this.flag.equals(ACTIVE);
    }
    public boolean isHistory(){
        return this.flag.equals(HISTORY);
    }
    public boolean isForward(){
        return this.flag.equals(FORWARD);
    }
    public boolean isSystem(){
        return this.flag.equals(SYSTEM);
    }
}
