package com.mall.order.biz.handler;

import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constants.OrderConstants;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dto.CartProductDto;
import com.mall.order.utils.GlobalIdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * 初始化订单 生成订单
 */

@Slf4j
@Component
public class InitOrderHandler extends AbstractTransHandler {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GlobalIdGeneratorUtil globalIdGeneratorUtil;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Transactional
    public boolean handle(TransHandlerContext context) {
        CreateOrderContext createOrderContext = (CreateOrderContext) context;
        Order order = new Order();
//        String orderId = UUID.randomUUID().toString();
        String orderId = globalIdGeneratorUtil.getNextSeq("orderId", 1);
        order.setOrderId(orderId);
        order.setUserId(createOrderContext.getUserId());
        order.setBuyerNick(createOrderContext.getBuyerNickName());
        order.setPayment(createOrderContext.getOrderTotal());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setStatus(OrderConstants.ORDER_STATUS_INIT);
        orderMapper.insert(order);

        List<CartProductDto> cartProductDtoList = createOrderContext.getCartProductDtoList();
        for (CartProductDto cartProductDto : cartProductDtoList) {
            OrderItem orderItem = new OrderItem();
//            String orderItemId = UUID.randomUUID().toString();
            String orderItemId = globalIdGeneratorUtil.getNextSeq("orderItemId", 1);
            orderItem.setId(orderItemId);
            orderItem.setItemId(cartProductDto.getProductId());
            orderItem.setOrderId(orderId);
            orderItem.setNum(cartProductDto.getProductNum().intValue());
            orderItem.setPicPath(cartProductDto.getProductImg());
            orderItem.setPrice(cartProductDto.getSalePrice().doubleValue());
            orderItem.setStatus(1);
            orderItem.setTitle(cartProductDto.getProductName());
            BigDecimal fee = cartProductDto.getSalePrice().multiply(new BigDecimal(cartProductDto.getProductNum()));
            orderItem.setTotalFee(fee.doubleValue());
            orderItemMapper.insert(orderItem);
        }
        createOrderContext.setOrderId(orderId);
        return true;
    }
}
