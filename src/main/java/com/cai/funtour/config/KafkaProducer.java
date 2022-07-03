package com.cai.funtour.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/1 15:32
 * @description：kafka生产者
 */

@Component
@Slf4j
public class KafkaProducer {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Object msg, @Nullable Integer partition){
        String message = "";
        if (msg instanceof String){
            message = (String)msg;
        }else{
            message = JSON.toJSONString(msg);
        }

        ListenableFuture<SendResult<String, String>> ack = kafkaTemplate.send(topic, partition, "" + partition, message);
        ack.addCallback(ok -> log.info("投递消息成功：topic:{};msg:{};partition:{}", topic, msg, partition),
                err -> log.error("投递消息失败：topic:{};msg:{};partition:{}\n失败原因：错误类型：{};错误原因：{};错误信息：{}", topic, msg, partition, err.getClass(), err.getCause(),err.getMessage()));
    }
}
