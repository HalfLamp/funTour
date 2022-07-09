package com.cai.funtour.consumer;

import com.alibaba.fastjson.JSON;
import com.cai.funtour.po.Label;
import com.cai.funtour.service.LabelService;
import com.cai.funtour.tools.LabelType;
import com.cai.funtour.tools.Tools;
import com.cai.funtour.tools.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/4 15:20
 * @description：景点消息消费
 */
@Component
@Slf4j
public class SightConsumer {

    @Autowired
    LabelService labelService;

    // 用户偏好分析
    @KafkaListener(topics = {Tools.KAFKA_TOPIC_PREFERENCE})
    public void preferenceOnMessage(ConsumerRecord<String, String> record, Acknowledgment ack, @Header(Tools.TRACE_ID) String traceId) {
        MDC.put(Tools.TRACE_ID, traceId);

        String msg = record.value();
        Map<String, String> data = JSON.parseObject(msg, Map.class);
        String userId = data.get("userId");
        String sightId = data.get("sightId");
        Double weight = Double.valueOf(data.get("weight"));

        // 查询景点对应的标签以及用户对应的标签，若用户启用的标签未满8个，则直接增加，若已满，则进行根据权重进行更新。
        // 景点标签
        List<Label> sightLabels = labelService.getLabel(sightId, LabelType.SIGHT);

        labelService.updateUserLabelWeight(sightLabels, weight, userId);
        // 更新使用中的用户标签权重(选出前8名的标签，计算所占百分比，并新增/更新状态为1的记录)
        List<Label> userLabels = labelService.getLabel(userId, LabelType.USER);
        List<Label> inuse = userLabels.stream().filter(Label::getNotInuse).limit(8).collect(Collectors.toList());

        labelService.updateWeight(userId, inuse);
        // 确认消费
        ack.acknowledge();
        log.info("消费成功！topic:{};group:{},msg:{}", record.topic(), KafkaUtil.DEFAULT_GROUP, msg);
    }

    // 热门景点分析
    //@KafkaListener(topics = {Tools.KAFKA_TOPIC_HOTSIGHT})
    public void hotOnMessage(ConsumerRecord<String, String> record, Acknowledgment ack, @Header(Tools.TRACE_ID) String traceId) {
        MDC.put(Tools.TRACE_ID, traceId);

        String msg = record.value();
        Map<String, String> data = JSON.parseObject(msg, Map.class);
        String sightId = data.get("sightId");
        // 确认消费
        ack.acknowledge();
        log.info("消费成功！topic:{};group:{},msg:{}", record.topic(), KafkaUtil.DEFAULT_GROUP, msg);
    }
}
