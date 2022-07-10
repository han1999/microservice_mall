package com.mall.order.services;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.order.OrderCoreService;
import com.mall.order.biz.TransOutboundInvoker;
import com.mall.order.biz.context.AbsTransHandlerContext;
import com.mall.order.biz.factory.OrderProcessPipelineFactory;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.constants.OrderConstants;
import com.mall.order.converter.OrderConverter;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.OrderShippingMapper;
import com.mall.order.dal.persistence.StockMapper;
import com.mall.order.dto.*;
import com.mall.order.utils.ExceptionProcessorUtils;
import com.mall.order.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    StockMapper stockMapper;


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
			 * 忘记PageHelper怎么用的了
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

//				Example orderItemExample = new Example(OrderItem.class);
//				orderItemExample.createCriteria().andEqualTo("orderId", order.getOrderId());
//				List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
                List<OrderItem> orderItems = orderItemMapper.queryByOrderId(order.getOrderId());

                List<OrderItemDto> orderItemDtos = orderConverter.items2dtos(orderItems);
				orderDetailInfo.setOrderItemDto(orderItemDtos);

				OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(order.getOrderId());
				OrderShippingDto orderShippingDto = orderConverter.shipping2dto(orderShipping);

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

	@Override
	public OrderDetailResponse getOrderDetail(OrderDetailRequest request) {
		OrderDetailResponse response = new OrderDetailResponse();
		try {
			request.requestCheck();
			String orderId = request.getOrderId();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			response.setUserId(order.getUserId());
			response.setOrderTotal(order.getPayment().longValue());
			OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
			response.setUserName(orderShipping.getReceiverName());
			response.setTel(orderShipping.getReceiverPhone());
			response.setStreetName(orderShipping.getReceiverAddress());

//			Example orderItemExample = new Example(OrderItem.class);
//			/**
//			 * property 说明是 类里面的属性
//			 */
//			orderItemExample.createCriteria().andEqualTo("orderId", orderId);
//			List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
            List<OrderItem> orderItems = orderItemMapper.queryByOrderId(orderId);
            List<OrderItemDto> orderItemDtos = orderConverter.items2dtos(orderItems);
			response.setGoodsList(orderItemDtos);
			return ResponseUtils.setCodeAndMsg(response, OrderRetCode.SUCCESS);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.getOrderDetail occurs Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

    @Override
    public DeleteOrderResponse deleteOrder(DeleteOrderRequest request) {
        /**
         * 按照以下顺序反着来
         */
  /*      pipeline.addLast(validateHandler);
        pipeline.addLast(subStockHandler);
        pipeline.addLast(initOrderHandler);
        pipeline.addLast(logisticalHandler);
        pipeline.addLast(clearCartItemHandler);
        pipeline.addLast(sendMessageHandler);*/
        DeleteOrderResponse response = new DeleteOrderResponse();
        try {
            request.requestCheck();
			String orderId = request.getOrderId();
			int orderShippingDelete = orderShippingMapper.deleteByPrimaryKey(orderId);
			/**
             * 通过orderItem对stock进行更改
             */
            List<OrderItem> orderItems = orderItemMapper.queryByOrderId(orderId);
			for (OrderItem orderItem : orderItems) {
				Long itemId = orderItem.getItemId();
				Integer num = orderItem.getNum();

				Stock stock = new Stock();
				stock.setItemId(itemId);
				stock.setLockCount(-num);
				stock.setStockCount(num.longValue());
				int stockUpdate = stockMapper.updateStock(stock);
				if (stockUpdate == 0) {
					return ResponseUtils.setCodeAndMsg(response, OrderRetCode.DB_SAVE_EXCEPTION);
				}
			}
			Example orderItemExample = new Example(OrderItem.class);
			orderItemExample.createCriteria().andEqualTo("orderId", orderId);
			int orderItemDelete= orderItemMapper.deleteByExample(orderItemExample);

			int orderDelete = orderMapper.deleteByPrimaryKey(orderId);
			if (orderShippingDelete == 0 || orderItemDelete == 0 || orderDelete == 0) {
				return ResponseUtils.setCodeAndMsg(response, OrderRetCode.DB_EXCEPTION);
			}
			return ResponseUtils.setCodeAndMsg(response, OrderRetCode.SUCCESS);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.deleteOrder occurs Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

	@Override
	public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
		CancelOrderResponse response = new CancelOrderResponse();
		try {
			request.requestCheck();

			String orderId = request.getOrderId();
			/**
			 * orderShipping不改了， orderItem和order也不删除，而是改变状态
			 */
			/**
			 * 库存锁定状态 1库存已锁定 2库存已释放 3-库存减扣成功
			 */

			/**
			 * 更新时间要写上
			 */
			/**
			 * 原本updateStockStatus返回的是void， 我改成int， 应该没有问题
			 */
			int orderItemUpdate = orderItemMapper.updateStockStatus(OrderConstants.ORDERITEM_STATUS_STOCK_FREE, orderId, new Date());

			List<OrderItem> orderItems = orderItemMapper.queryByOrderId(orderId);
			for (OrderItem orderItem : orderItems) {
				Integer num = orderItem.getNum();
				Stock stock = new Stock();
				stock.setItemId(orderItem.getItemId());
				stock.setStockCount(num.longValue());
				stock.setLockCount(-num);
				int stockUpdate = stockMapper.updateStock(stock);
				if (stockUpdate == 0) {
					return ResponseUtils.setCodeAndMsg(response, OrderRetCode.DB_SAVE_EXCEPTION);
				}
			}

			Order order = new Order();
			order.setOrderId(orderId);
			order.setStatus(OrderConstants.ORDER_STATUS_TRANSACTION_CANCEL);
			order.setUpdateTime(new Date());
			int orderUpdate = orderMapper.updateByPrimaryKeySelective(order);
			if (orderItemUpdate == 0 || orderUpdate == 0) {
				return ResponseUtils.setCodeAndMsg(response, OrderRetCode.DB_SAVE_EXCEPTION);
			}
			return ResponseUtils.setCodeAndMsg(response, OrderRetCode.SUCCESS);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.cancelOrder occurs Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

}
