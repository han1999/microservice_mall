package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.shopping.constants.ShoppingRetCode;
import lombok.Data;

/**
 * Created on 2019/7/23.
 */
@Data
public class AddCartRequest extends AbstractRequest{

    private Long userId;
    private Long itemId;
    //cartProductDto中， productNum是Long的， 这前后都不统一，要来回转，设计失误
    private Integer num;

    @Override
    public void requestCheck() {
        if (userId == null || userId < 0 || itemId == null || itemId < 0 || num == null || num < 0) {
            throw new ValidateException(
                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),
                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage()
            );
        }
    }
}
