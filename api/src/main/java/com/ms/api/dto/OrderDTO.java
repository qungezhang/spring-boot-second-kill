package com.ms.api.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderDTO implements Serializable {

    private Integer id;

    private Integer goodId;

    private String goodsName;

    private Date createTime;

    public OrderDTO(Integer id, Integer goodId, String goodsName, Date createTime) {
        this.id = id;
        this.goodId = goodId;
        this.goodsName = goodsName;
        this.createTime = createTime;
    }

    public OrderDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
