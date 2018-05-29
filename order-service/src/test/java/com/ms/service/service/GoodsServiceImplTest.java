package com.ms.service.service;

import com.ms.api.dto.GoodsDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceImplTest {
    @Autowired
    private GoodsServiceImpl goodsSerivce;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void testRedis(){
       redisTemplate.opsForValue().getOperations().delete("goods_2");
    }

    @Test
    public void list(){
        List<GoodsDTO> goodsDTOS = goodsSerivce.list();
        Assert.assertNotNull(goodsDTOS);
    }
}