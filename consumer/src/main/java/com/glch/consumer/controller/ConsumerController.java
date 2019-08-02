package com.glch.consumer.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerController {

    @KafkaListener(topics = {"ADDRLIST_0"})
    public void listen(ConsumerRecord record){
        //实时消费的消息体
        Object value = record.value();
        System.out.println(value);
        System.out.println(45);
        /**
         相关业务代码操作
         */
    }
}
