package com.mall.order;

import com.mall.order.dto.*;

/**
 * 订单相关业务
 */
public interface OrderCoreService {

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    CreateOrderResponse createOrder(CreateOrderRequest request);

    OrderListResponse getAllOrders(OrderListRequest request);

    OrderDetailResponse getOrderDetail(OrderDetailRequest request);

    DeleteOrderResponse deleteOrder(DeleteOrderRequest request);

    CancelOrderResponse cancelOrder(CancelOrderRequest request);

    PayOrderSuccessResponse payOrderSuccess(PayOrderSuccessRequest request);

    PayOrderFailResponse payOrderFail(PayOrderFailRequest request);
}
