package com.mall.shopping.dto;


import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 * Created on 2019/8/8
 * 21:46.
 */
@Data
public class AllProductCateRequest extends AbstractRequest {
    /**
     * sort具体干什么用，现在还不知道
     */
    private String sort;

    @Override
    public void requestCheck() {
//        if (StringUtils.isBlank(sort)) {
//            throw new ValidateException(
//                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),
//                    ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage()
//            );
//        }
    }
}
