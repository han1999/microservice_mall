package com.mall.order.mq.delay;

import com.mall.order.constants.OrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/14
 **/

@Component
@Slf4j
public class DelayOrderCancelProducer {
    private DefaultMQProducer producer;

    @PostConstruct
    public void init() {
        producer = new DefaultMQProducer(OrderConstants.PRODUCER_GROUP);
        producer.setNamesrvAddr(OrderConstants.NAME_SERVER);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送订单超时自动取消的消息
     * 有一个很隐蔽的bug， 本地安装的rocketmq版本是4.4.0， maven引入的依赖的版本是4.3.0，导致出现route info报错
     * @param orderId
     * @return
     */
    public boolean sendDelayOrderMessage(String orderId) {
        log.info("enter sendDelayOrderMessage {}", orderId);
        Message message = new Message(OrderConstants.DELAY_TOPIC, orderId.getBytes(Charset.forName("utf-8")));
        message.setDelayTimeLevel(OrderConstants.DELAY_LEVEL);
        SendResult sendResult = null;
        message.putUserProperty("startTime", System.currentTimeMillis() + "");
        try {
            sendResult = producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            //消息发送成功
            log.info("sendResult:{}", sendResult);
            return true;
        }
        return false;
    }

}
