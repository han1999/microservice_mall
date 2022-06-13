package com.mall.order.dto;/**
 *
 */

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 */
@Data
public class CreateOrderResponse extends AbstractResponse{

    private String orderId;
}
