package com.ms.service.service;

import com.google.gson.Gson;
import com.ms.api.Service.GoodsService;
import com.ms.api.Service.OrderService;
import com.ms.api.dto.GoodsDTO;
import com.ms.service.dao.OrderMapper;
import com.ms.service.pojo.Order;
import com.ms.service.util.Const;
import com.ms.service.util.RedisLock;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private Gson gson;

    @Value("${kafka.order.topic}")
    private String orderTopic;

    @Override
    public int insert(Integer goodsId,Integer num) {
        long expireTime = System.currentTimeMillis()+5000;
        if(redisLock.lock("order_goods_lock_"+goodsId,String.valueOf(expireTime))) {
            //查询goods
            GoodsDTO goodsDTO = goodsService.get(goodsId);
            //校验库存
            if(checkStock(goodsDTO,num)) {
                Order order = new Order();
                order.setGoodId(goodsId);
                order.setGoodsName(goodsDTO.getName());
                //扣库存
                int sale = goodsDTO.getSale() + num;
                goodsDTO.setSale(sale);
                goodsService.update(goodsDTO);

                //创建订单
                int rowCount = orderMapper.insertSelective(order);
                redisLock.unlock("order_goods_lock_" + goodsId, String.valueOf(expireTime));
            }
        }else{
            logger.info("当前人数过多，请稍后再试");
        }
        return 1;
    }

    @Override
    public int insertByKafka(Integer goodsId, Integer num) {

        long expireTime = System.currentTimeMillis()+5000;
        //加锁
        if(redisLock.lock(Const.Goods.GOODS_KEY+goodsId,String.valueOf(expireTime))){
            //校验库存
            GoodsDTO goodsDTO = goodsService.get(goodsId);
            if(checkStock(goodsDTO,num)){
                //更新库存
                int sale = goodsDTO.getSale() + num;
                goodsDTO.setSale(sale);
                goodsService.update(goodsDTO);
                Order order = new Order();
                order.setGoodId(goodsId);
                order.setGoodsName(goodsDTO.getName());
                //利用kafka创建订单---传json字符串
                kafkaProducer.send(new ProducerRecord(orderTopic,gson.toJson(order)));
            }
            //解锁
            redisLock.unlock(Const.Goods.GOODS_KEY+goodsId,String.valueOf(expireTime));
        }else{
            logger.info("当前人数过多，请稍后再试");
        }
        return 1;
    }

    public boolean checkStock(GoodsDTO goodsDTO,Integer num){
        int stock = goodsDTO.getCount() - goodsDTO.getSale();
        if (stock < 1 || stock < num) {
            logger.error("库存不足啦，下单失败！");
            // redisLock.unlock("order_goods_lock_"+goodsId,String.valueOf(expireTime));
            return false;
        }
        return true;
    }
}
