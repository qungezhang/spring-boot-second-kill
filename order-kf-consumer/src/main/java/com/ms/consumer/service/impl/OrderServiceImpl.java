package com.ms.consumer.service.impl;

import com.ms.api.dto.OrderDTO;
import com.ms.consumer.dao.OrderMapper;
import com.ms.consumer.pojo.Order;
import com.ms.consumer.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("kafkaOrderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int createOrderByKafka(OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO,order);
        return orderMapper.insertSelective(order);
    }
}
