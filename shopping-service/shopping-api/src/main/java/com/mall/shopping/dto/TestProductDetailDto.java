package com.mall.shopping.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TestProductDetailDto implements Serializable {
    String name;

    BigDecimal price;

    String imgUrl;

}
