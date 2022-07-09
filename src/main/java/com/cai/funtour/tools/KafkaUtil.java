package com.cai.funtour.tools;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/6 9:35
 * @description：kafka工具类
 */
@Component
public class KafkaUtil {
    public static String DEFAULT_GROUP;

    @Value("${spring.kafka.consumer.properties.group.id}")
    public void setGroup(String group){
        KafkaUtil.DEFAULT_GROUP = group;
    }

    public static void setTraceId(Headers headers){
        for (Header header : headers.headers(Tools.TRACE_ID)) {
            if (header.key().equals(Tools.TRACE_ID)){
                MDC.put(Tools.TRACE_ID, new String(header.value(), Charset.forName("UTF-8")));
            }
        }
    }
}
