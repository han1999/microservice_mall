package com.mall.order;

import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/17
 **/

public interface OrderPromoService {

    CreateSeckillOrderResponse createPromoOrder(CreateSeckillOrderRequest createSeckillOrderRequest);
}
