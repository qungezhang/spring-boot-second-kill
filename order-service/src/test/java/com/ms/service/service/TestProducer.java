package com.ms.service.service;


import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author: zhangj
 * @Description:
 * @Date: Create in 9:42 2018-05-26
 */
public class TestProducer {


    //创建topic
    public static void createTopic(){
        Properties prop = new Properties();
        prop.put("bootstrap.servers","120.79.69.44:9092");
        AdminClient adminClient = AdminClient.create(prop);
        List<NewTopic> topics = Lists.newArrayList();
        NewTopic topic = new NewTopic("my_test",1,(short)1);
        topics.add(topic);
        //创建
        CreateTopicsResult result = adminClient.createTopics(topics);
        try {
            result.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //创建生产者
    public static Producer createProducer(){
        Properties prop = new Properties();
        prop.put("bootstrap.servers","120.79.69.44:9092");
        prop.put("asks","all");
        prop.put("retries",0);
        prop.put("batch.size",16384);
        prop.put("linger.ms",1);
        prop.put("buffer.memory",33554432);
        prop.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        Producer producer = new KafkaProducer(prop);
        return producer;
    }

    public static void main(String[] args) {
        //createTopic();
        Producer producer = createProducer();
        ProducerRecord producerRecord = new ProducerRecord("tes","bb","aaa");
        producer.send(producerRecord);
        producer.close();
    }
}
