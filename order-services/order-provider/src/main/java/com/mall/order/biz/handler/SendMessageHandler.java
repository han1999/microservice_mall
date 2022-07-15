package com.mall.order.biz.handler;

import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.mq.delay.DelayOrderCancelProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 利用mq发送延迟取消订单消息
 * @Date: 2019-09-17 23:14
 **/
@Component
@Slf4j
public class SendMessageHandler extends AbstractTransHandler {

	@Autowired
	DelayOrderCancelProducer producer;

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	public boolean handle(TransHandlerContext context) {
		//获取生产的订单Id， 发送一个包含订单Id的延迟消息，延迟半小时
		CreateOrderContext createOrderContext = (CreateOrderContext) context;
		String orderId = createOrderContext.getOrderId();

		//调用方法，发送消息
		return producer.sendDelayOrderMessage(orderId);
	}
}