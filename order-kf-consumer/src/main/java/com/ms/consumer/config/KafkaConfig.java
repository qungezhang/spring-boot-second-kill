package com.ms.consumer.config;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.ms.consumer.kafka.KafkaOrder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.util.Map;

@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String servers;

    @Value("${kafka.group.id}")
    private String groupId;

    @Value("${kafka.enable.auto.commit}")
    private boolean autoCommit;

    @Value("${kafka.auto.commit.interval.ms}")
    private int intervalMs;

    @Value("${kafka.consumer.session.timeout}")
    private int sessionTimeout;

    @Value("${kafka.consumer.currentcy}")
    private int currency;

    @Value("${kafka.order.topic}")
    private String orderTopic;

    @Value("${kafka.consumer.threadnum}")
    private int threadNum;

    public Map<String,Object> consumerConfig(){
        Map<String,Object> props = Maps.newHashMap();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,intervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,sessionTimeout);
        return props;
    }

  /*  @Bean
    public ConsumerFactory<String,Object> consumerFactory(){
        return new DefaultKafkaConsumerFactory<String,Object>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,Object>> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,Object> factory = new ConcurrentKafkaListenerContainerFactory<String,Object>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(currency);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }*/



    //线程池kafka订阅创建订单
    @Bean
    public KafkaOrder kafkaOrder(){
        return new KafkaOrder(threadNum,orderTopic,servers,groupId);
    }

    @Bean
    public Gson build(){
        return new Gson() ;
    }

}
