package com.ms.consumer.service;

import com.ms.api.dto.OrderDTO;

public interface OrderService {

    int createOrderByKafka(OrderDTO orderDTO);
}
