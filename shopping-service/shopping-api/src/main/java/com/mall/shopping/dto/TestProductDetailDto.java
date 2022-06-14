package com.mall.shopping.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TestProductDetailDto implements Serializable {
    private String name;

    private BigDecimal price;

    private String imgUrl;

}
