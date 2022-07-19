package com.mall.order.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.order.constant.OrderRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Description
 **/
@Data
public class CreateSeckillOrderRequest extends AbstractRequest {
    private Long userId;
    private String username;
    private Long productId;
    private BigDecimal price;

    /**
     * 以下属性都是我新加入的
     */
    private Long addressId;
    private String tel;
    private String streetName;
    @Override
    public void requestCheck() {

        if (userId == null || StringUtils.isBlank(username) || productId == null || price == null) {
            throw new ValidateException(OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }

    }
}