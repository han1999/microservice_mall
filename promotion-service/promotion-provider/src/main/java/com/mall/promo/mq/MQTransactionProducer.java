package com.mall.promo.mq;


import com.alibaba.fastjson.JSON;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.promo.cache.CacheManager;
import com.mall.promo.dal.entitys.PromoItem;
import com.mall.promo.dal.persistence.PromoItemMapper;
import com.mall.promo.dto.CreatePromoOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

@Component
@Slf4j
public class MQTransactionProducer {

    @Value("${mq.nameserver.addr}")
    private String addr;

    @Value("${mq.topic-name}")
    private String topic;

    @Value("${mq.producer.group}")
    private String producerGroup;

    private TransactionMQProducer transactionMQProducer;

    @Autowired
    private PromoItemMapper promoItemMapper;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        transactionMQProducer = new TransactionMQProducer(producerGroup);
        transactionMQProducer.setNamesrvAddr(addr);


        // 注册事物监听器
        transactionMQProducer.setTransactionListener(new TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                // 执行本地事物，扣减库存
                HashMap<String, Long> argsMap = (HashMap<String, Long>) arg;
                Long productId = argsMap.get("productId");
                Long psId = argsMap.get("psId");

                Example example = new Example(PromoItem.class);
                example.createCriteria()
                        .andEqualTo("itemId", productId)
                        .andEqualTo("psId", psId);

                // 获取分布式锁
                String lockKey = "promo_item_stock_lock:" + psId + "_" + productId;
                RLock lock = redissonClient.getLock(lockKey);

                lock.lock();
                try {
                    int effectiveRow = promoItemMapper.decreaseStock(productId, psId);
                    String key = "promo_order_id_"  + message.getTransactionId();
                    if (effectiveRow < 1) {
                        String value = "fail";
                        cacheManager.setCache(key,value,1);
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    String value = "success";
                    cacheManager.setCache(key,value,1);
                    return LocalTransactionState.COMMIT_MESSAGE;
                } finally {
                    lock.unlock();
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                String key = "promo_order_id_" + messageExt.getTransactionId();

                String value = cacheManager.checkCache(key);
                if ("fail".equals(value)) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                if ("success".equals(value)) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }

                return LocalTransactionState.UNKNOW;
            }
        });

        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /*
         发送事物型消息
     */
    public Boolean sendPromoOrderTransaction(CreateSeckillOrderRequest request, Long psId, Long productId) {

        // 构建参数
        //HashMap<String, Object> map = generateMap(request);

        Message message = new Message(topic, JSON.toJSONString(request).getBytes(Charset.forName("utf-8")));

        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("productId",productId);
        argsMap.put("psId",psId);

        TransactionSendResult transactionSendResult = null;
        try {
            // 发送事务型消息
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);
            log.info("成功发送秒杀消息：" + transactionSendResult);
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        if (transactionSendResult == null || transactionSendResult.getLocalTransactionState() == null) {
            return false;
        }
        // 查看事务型消息的发送结果
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        if (localTransactionState.equals(LocalTransactionState.COMMIT_MESSAGE)) {
            return true;
        }else if (localTransactionState.equals(LocalTransactionState.ROLLBACK_MESSAGE)) {
            return false;
        }else {
            return false;
        }
    }

    private HashMap<String, Object> generateMap(CreatePromoOrderRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",request.getUsername());
        map.put("userId",request.getUserId());
        map.put("productId",request.getProductId());
        map.put("price",request.getPromoPrice());
        map.put("addressId",request.getAddressId());
        map.put("tel",request.getTel());
        map.put("streetName",request.getStreetName());
        return map;
    }

}
