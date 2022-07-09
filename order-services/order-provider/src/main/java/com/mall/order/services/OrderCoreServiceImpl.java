package com.mall.order.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.order.OrderCoreService;
import com.mall.order.biz.TransOutboundInvoker;
import com.mall.order.biz.context.AbsTransHandlerContext;
import com.mall.order.biz.factory.OrderProcessPipelineFactory;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.converter.OrderConverter;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.OrderShippingMapper;
import com.mall.order.dto.*;
import com.mall.order.utils.ExceptionProcessorUtils;
import com.mall.order.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 *  cskaoyan
 */
@Slf4j
@Component
@Service(cluster = "failfast")
public class OrderCoreServiceImpl implements OrderCoreService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	OrderItemMapper orderItemMapper;

	@Autowired
	OrderShippingMapper orderShippingMapper;

	@Autowired
    OrderProcessPipelineFactory orderProcessPipelineFactory;

	@Autowired
	OrderConverter orderConverter;


	/**
	 * 创建订单的处理流程
	 *
	 * @param request
	 * @return
	 */
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		CreateOrderResponse response = new CreateOrderResponse();
		try {
			//创建pipeline对象
			TransOutboundInvoker invoker = orderProcessPipelineFactory.build(request);

			//启动pipeline
			invoker.start(); //启动流程（pipeline来处理）

			//获取处理结果
			AbsTransHandlerContext context = invoker.getContext();

			//把处理结果转换为response
			response = (CreateOrderResponse) context.getConvert().convertCtx2Respond(context);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.createOrder Occur Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

	@Override
	public OrderListResponse getAllOrders(OrderListRequest request) {
		OrderListResponse response = new OrderListResponse();
		try {
			/**
			 * 忘记PageHelper怎么用的了 (使用不当好像会导致servlet映射不到）
			 * 1. mybatis执行之前用 PageHelper.start
			 * 2. new PageInfo<>()....
			 */
			request.requestCheck();
			PageHelper.startPage(request.getPage(), request.getSize());
			Example example = new Example(Order.class);
			example.createCriteria().andEqualTo("userId", request.getUserId());
//			example.setOrderByClause(request.getSort());
			List<Order> orderList = orderMapper.selectByExample(example);

			List<OrderDetailInfo> detailInfoList = new ArrayList<>();
			for (Order order : orderList) {
				OrderDetailInfo orderDetailInfo = orderConverter.order2detail(order);

				Example orderItemExample = new Example(OrderItem.class);
				orderItemExample.createCriteria().andEqualTo("orderId", order.getOrderId());
				List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);

				List<OrderItemDto> orderItemDtos = orderConverter.item2dto(orderItems);
				orderDetailInfo.setOrderItemDto(orderItemDtos);

				Example orderShippingExample = new Example(OrderShipping.class);
				orderShippingExample.createCriteria().andEqualTo("orderId", order.getOrderId());
				List<OrderShipping> orderShippings = orderShippingMapper.selectByExample(orderShippingExample);
				OrderShippingDto orderShippingDto = orderConverter.shipping2dto(orderShippings.get(0));

				orderDetailInfo.setOrderShippingDto(orderShippingDto);
				detailInfoList.add(orderDetailInfo);
			}
//			response.setDetailInfoList(detailInfoList);
			response.setData(detailInfoList);
			PageInfo<Order> orderPageInfo = new PageInfo<>(orderList);
			response.setTotal(orderPageInfo.getTotal());
			return ResponseUtils.setCodeAndMsg(response, OrderRetCode.SUCCESS);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.getAllOrders occurs Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

}
