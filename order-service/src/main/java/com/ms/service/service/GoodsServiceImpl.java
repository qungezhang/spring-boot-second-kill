package com.ms.service.service;

import com.google.common.collect.Lists;
import com.ms.api.Service.GoodsService;
import com.ms.service.dao.GoodsMapper;
import com.ms.api.dto.GoodsDTO;
import com.ms.service.pojo.Goods;
import com.ms.service.util.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("goodsSerivce")
public class GoodsServiceImpl implements GoodsService {

    @Resource
    RedisTemplate<String,Object> redisTemplate;


    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsDTO> list() {
        List<GoodsDTO> goodsDTOS = (List<GoodsDTO>)redisTemplate.opsForValue().get(Const.Goods.GOODS_LIST_KEY);
        if(goodsDTOS != null){
            return goodsDTOS;
        }
        List<Goods> goodsList = goodsMapper.selectGoodsList();
        List<GoodsDTO> goodsDTOList = Lists.newArrayList();
        for(Goods goods : goodsList){
            GoodsDTO goodsDTO = new GoodsDTO();
            BeanUtils.copyProperties(goods,goodsDTO);
            goodsDTOList.add(goodsDTO);
        }
        //放入缓存
        redisTemplate.opsForValue().set(Const.Goods.GOODS_LIST_KEY,goodsDTOList);
        return goodsDTOList;
    }

    @Override
    public int update(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO,goods);
        //更新缓存
        redisTemplate.opsForValue().set(Const.Goods.GOODS_KEY+goodsDTO.getId(),goodsDTO);
        return goodsMapper.updateByPrimaryKey(goods);
    }

    @Override
    public GoodsDTO get(Integer goodsId) {
        GoodsDTO goodsDTO = (GoodsDTO)redisTemplate.opsForValue().get(Const.Goods.GOODS_KEY+goodsId);
        if(goodsDTO != null){
            return goodsDTO;
        }

        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        goodsDTO = new GoodsDTO();
        BeanUtils.copyProperties(goods,goodsDTO);
        //放入缓存
        redisTemplate.opsForValue().set(Const.Goods.GOODS_KEY+goodsId,goodsDTO);
        return goodsDTO;
    }

    public static void main(String[] args) {
        GoodsDTO goodsDTO = new GoodsDTO();
        Goods goods  = new Goods(1,"aa",10,0,0);
        BeanUtils.copyProperties(goods,goodsDTO);
        System.out.println(goodsDTO);
    }
}
