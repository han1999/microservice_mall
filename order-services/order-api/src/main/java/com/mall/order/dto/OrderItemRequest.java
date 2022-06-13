package com.mall.order.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.order.constant.OrderRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author heps
 */
@Data
public class OrderItemRequest extends AbstractRequest {

    private String orderItemId;

    @Override
    public void requestCheck() {
        if (StringUtils.isEmpty(orderItemId)) {
            throw new ValidateException(OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
