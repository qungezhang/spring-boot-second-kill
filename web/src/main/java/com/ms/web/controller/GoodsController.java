package com.ms.web.controller;

import com.ms.api.Service.GoodsService;
import com.ms.api.dto.GoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/list")
    public List<GoodsDTO> list(){
        return goodsService.list();
    }
}
