package com.ms.consumer.kafka;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.ms.api.dto.OrderDTO;
import com.ms.consumer.service.OrderService;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class KafkaOrderTask implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(KafkaOrderTask.class);
    /**
     * 每个线程维护KafkaConsumer实例
     */
    private final Consumer<String, Object> consumer;

    private OrderService orderService;

    private Gson gson;

    //构造函数初始化
    public KafkaOrderTask(String servers,String topic,String groupId){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,1000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,6000);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<String, Object>(props);
        this.gson = SpringBeanFactory.getBean(Gson.class);
        //通过applicationContextAware获取bean,多线程中无法通过spring容器注入
        this.orderService = SpringBeanFactory.getBean(OrderService.class);
        //订阅topic
        consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
        boolean flag = true;
        while(flag){
            //超时时间100ms
            ConsumerRecords<String,Object> consumerRecords = consumer.poll(100);
            for(ConsumerRecord<String, Object> record : consumerRecords){
                logger.info("偏移量={}，key={},value={}",record.offset(),record.key(),record.value());
                createOrder(record.value().toString());
            }
        }
    }

    //创建订单
    public void createOrder(String value){
        try{
            OrderDTO orderDTO = gson.fromJson(value,OrderDTO.class);
            orderService.createOrderByKafka(orderDTO);
        }catch(Exception e){
            logger.info("异步创建订单失败!");
        }
    }
}
