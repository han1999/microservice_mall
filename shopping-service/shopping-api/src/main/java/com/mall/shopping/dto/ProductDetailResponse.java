package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 */

@Data
public class ProductDetailResponse extends AbstractResponse {
    private ProductDetailDto productDetailDto;
}
