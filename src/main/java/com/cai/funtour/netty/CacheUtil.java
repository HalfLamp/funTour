package com.cai.funtour.netty;

import io.netty.channel.Channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/27 9:57
 * @description：Netty缓存
 */
public class CacheUtil {
    /**
     * 缓存本地channel
     */
    private static Map<String, Channel> cachedChannel = Collections.synchronizedMap(new HashMap<>());

    public static boolean contains(String channelId){
        return cachedChannel.containsKey(channelId);
    }

    public static void put(String channelId, Channel channel){
        cachedChannel.put(channelId, channel);
    }

    public static Channel getChanel(String channelId){
        return cachedChannel.get(channelId);
    }

    public static void remove(String channelId){
        cachedChannel.remove(channelId);
    }
}
