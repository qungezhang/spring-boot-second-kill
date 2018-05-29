package com.ms.consumer;

import com.ms.consumer.kafka.KafkaOrder;
import com.ms.consumer.kafka.SpringBeanFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ms")
@MapperScan("com.ms.consumer.dao")
public class OrderKfConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderKfConsumerApplication.class, args);
        KafkaOrder kafkaOrder = SpringBeanFactory.getBean(KafkaOrder.class);
        kafkaOrder.execute();
    }
}
