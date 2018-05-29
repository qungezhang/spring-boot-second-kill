package com.ms.service.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaConfig {

    @Value("${kafka.servers}")
    private String servers;

    @Value("${kafka.retries}")
    private int retries;

    @Value("${kafka.batch.size}")
    private int batchSize;

    @Value("${kafka.linger}")
    private int linger;

    @Value("${kafka.buffer.memory}")
    private int bufferMemory;

    @Value("${kafka.swicth}")
    private boolean check ;

    public Map<String,Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        props.put(ProducerConfig.RETRIES_CONFIG,retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG,linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,bufferMemory);
        props.put("asks","all");
        props.put("metadata.broker.list", servers);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    /*public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<String,Object>(producerFactory());
    }*/

    @Bean
    public KafkaProducer<String,Object> kafkaProducer(){

        /*if (!check){
            return null ;
        }*/
        KafkaProducer kafkaProducer = new KafkaProducer<String,Object>(producerConfig());
        return kafkaProducer;
    }

}
