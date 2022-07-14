package com.mall.order.mq.delay;

import com.mall.order.OrderCoreService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.constants.OrderConstants;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dto.DeleteOrderRequest;
import com.mall.order.dto.DeleteOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/14
 **/
@Component
@Slf4j
public class DelayOrderCancelConsumer {
    private DefaultMQPushConsumer consumer;

    @Autowired
    OrderCoreService orderCoreService;

    @Autowired
    OrderMapper orderMapper;

    @PostConstruct
    public void init() {
        consumer = new DefaultMQPushConsumer(OrderConstants.CONSUMER_GROUP);
        consumer.setNamesrvAddr(OrderConstants.NAME_SERVER);
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 首先从消息中取出订单的orderId
                // 拿着orderId查询orderId对应的订单状态，如果订单状态是已支付或者已取消，那么什么都不做
                // 如果订单的状态是初始化状态，那说明用户还没有支付，我们就可以去取消订单了
                // 取消订单具体指： 1. 修改订单的状态为已取消 2. 还原可售卖库存，遍历被取消的订单中的商品条目
                long endTime = System.currentTimeMillis();
                MessageExt messageExt = list.get(0);
                String startTimeStr = messageExt.getProperty("startTime");
                long startTime = Long.parseLong(startTimeStr);
                double useTime = (endTime - startTime) / 1000.0;
                log.info("useTime: {}s", useTime);
                byte[] body = messageExt.getBody();
                String orderId = new String(body, 0, body.length, Charset.forName("utf-8"));
                log.info("enter consumeMessage: orderId {}", orderId);
                Order order = orderMapper.selectByPrimaryKey(orderId);
                Integer status = order.getStatus();
                if (OrderConstants.ORDER_STATUS_INIT == status) {
                    DeleteOrderRequest request = new DeleteOrderRequest();
                    request.setOrderId(orderId);
                    DeleteOrderResponse response = orderCoreService.deleteOrder(request);
                    if (OrderRetCode.SUCCESS.getCode().equals(response.getCode())) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        try {
            consumer.subscribe(OrderConstants.DELAY_TOPIC, "*");
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
