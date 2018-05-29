package com.ms.api.Service;

public interface OrderService {

    int insert(Integer goodsId,Integer num);

    int insertByKafka(Integer goodsId,Integer num);
}
