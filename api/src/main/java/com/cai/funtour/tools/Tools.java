package com.cai.funtour.tools;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 15:16
 * @description：字典类
 */
public class Tools {
    // system params
    public static final String TRACE_ID = "traceId";
    public static final String TOKEN = "token";
    public static final String JWT_IISUSER = "funtour";
    public static final String JWT_SUBJECT_USER = "normal_user";
    public static final String JWT_SUBJECT_SYSTEM = "manager_user";

    // redis keys
    public static final String CACHE_TOKEN = "FUNTOUR:USER:TOKEN_";

    // kafka topics name
    public static final String KAFKA_TOPIC_PREFERENCE = "funtour_sight_preference_r1p2";
    public static final String KAFKA_TOPIC_HOTSIGHT = "funtour_sight_hot_r1p2";
}
