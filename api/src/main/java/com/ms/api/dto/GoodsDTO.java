package com.ms.api.dto;

import java.io.Serializable;

public class GoodsDTO implements Serializable {

    private Integer id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;

    public GoodsDTO(Integer id, String name, Integer count, Integer sale, Integer version) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.sale = sale;
        this.version = version;
    }

    public GoodsDTO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
