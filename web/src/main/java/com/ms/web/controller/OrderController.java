package com.ms.web.controller;

import com.ms.api.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/insert")
    public int insert(Integer goodsId,Integer num){
        return orderService.insert(goodsId,num);
    }

    @RequestMapping("/create")
    public int create(Integer goodsId,Integer num){
        return orderService.insertByKafka(goodsId,num);
    }
}
