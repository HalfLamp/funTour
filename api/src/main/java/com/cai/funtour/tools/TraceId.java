package com.cai.funtour.tools;

import java.util.UUID;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 14:54
 * @description：生成日志文件traceid
 */
public class TraceId {
    public static String getTraceId(){
        String time = String.valueOf(System.currentTimeMillis());
        String uid = UUID.randomUUID().toString().replaceAll("-","");
        time = time.substring(5, time.length() - 1);
        return time + uid;
    }
}
