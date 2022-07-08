package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.shopping.constants.ShoppingRetCode;
import lombok.Data;

@Data
public class TestProductDetailRequest extends AbstractRequest {

    private Long productId;

    @Override
    public void requestCheck() {
        if (productId == null || productId < 0) {
            throw new ValidateException(
                    ShoppingRetCode.PARAMETER_VALIDATION_FAILED.getCode(),
                    ShoppingRetCode.PARAMETER_VALIDATION_FAILED.getMessage()
            );
        }

    }
}
