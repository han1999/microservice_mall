package com.mall.order.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.order.constant.OrderRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/15
 **/
@Data
public class PayOrderSuccessRequest extends AbstractRequest {
    private String orderId;
    @Override
    public void requestCheck() {
        if (StringUtils.isBlank(orderId)) {
            throw new ValidateException(OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(), OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
