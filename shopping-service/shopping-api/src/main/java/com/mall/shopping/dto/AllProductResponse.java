package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class AllProductResponse extends AbstractResponse {

    //    private List<ProductDto> productDtoList;
    private List<ProductDto> data;
    private Long total;
}
