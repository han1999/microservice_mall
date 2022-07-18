package com.mall.order.mq.promo;

import com.alibaba.fastjson.JSON;
import com.mall.order.OrderPromoService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;

import java.lang.String;
/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/18
 **/

@Component
public class MQTransactionConsumer {
    private DefaultMQPushConsumer consumer;

    @Value("${mq.consumer.group}")
    private String consumerGroup;

    @Value("${mq.nameserver.addr}")
    private String nameserverAddr;

    @Value("${mq.topic-name}")
    private String topicName;

    /**
     * 不是调用服务，而是调用方法
     */
    @Autowired
    OrderPromoService orderPromoService;

    @PostConstruct
    public void init() {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameserverAddr);

        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                byte[] body = messageExt.getBody();
                String data = new String(body, 0, body.length, Charset.forName("utf-8"));
                CreateSeckillOrderRequest request = JSON.parseObject(data, CreateSeckillOrderRequest.class);
                CreateSeckillOrderResponse response = orderPromoService.createPromoOrder(request);
                if (OrderRetCode.SUCCESS.getCode().equals(response.getCode())) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        try {
            consumer.subscribe(topicName, "*");
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
