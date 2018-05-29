package com.ms.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service ("redisLock")
public class RedisLock {


    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis分布式加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key,String value){
        //通过redis的setnx如果设置成功,说明key不存在获得锁，判断锁是否被占用
        if(stringRedisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        //currentValue = A,这是如果有两个线程他们value=B--当前value
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        //如果某个线程宕机了 一直占用锁避免死锁
        //判断之前的key是否过期如果过期允许线程竞争获取的锁通过----》getset
        if(!StringUtils.isEmpty(currentValue)
                && Long.valueOf(currentValue) < System.currentTimeMillis()){
            //getset获取上一个锁的时间
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key,value);
            //通过判断上一个锁的时间和最开始redis中的key的时间是否相等
            //如果相等说明还未被其他线程getset获得过---获取成功
            if(!StringUtils.isEmpty(oldValue)
                    &&oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }


    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key,String value){
        try{
            //获取当前redis中key对应的value
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            //如果当前value和要解锁的key对应的value一直说明是同一锁 释放锁
            if(StringUtils.isEmpty(currentValue)
                    && currentValue.equals(value)){
                //删除key---释放分布式锁
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch(Exception e){
            logger.error("释放锁异常，e={}",e);
        }
    }
}

