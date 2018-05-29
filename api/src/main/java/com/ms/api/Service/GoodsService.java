package com.ms.api.Service;

import com.ms.api.dto.GoodsDTO;

import java.util.List;

public interface GoodsService {

    List<GoodsDTO> list();

    int update(GoodsDTO goodsDTO);

    GoodsDTO get(Integer goodsId);
}
