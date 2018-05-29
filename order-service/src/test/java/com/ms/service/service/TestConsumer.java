package com.ms.service.service;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class TestConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers","120.79.69.44:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit","true");
        props.put("auto.commit.interval.ms","1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        final Consumer consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList("ORDER-TOPIC"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                //将偏移设置到最开始
                consumer.seekToBeginning(collection);
            }
        });

        while(true){
            //获取超时时间100ms
            ConsumerRecords<String,String> records = consumer.poll(100);
            for(ConsumerRecord<String,String> record :records){
                System.out.printf("偏移量=%d,key=%s,value=%s%n",record.offset(),record.key(),record.value());
            }
        }
    }
}
