package com.mall.order.constants;/**
 * Created by cskaoyan on 2019/7/30.
 */

/**
 *  cskaoyan
 */
public class OrderConstants {

    public static int ORDER_STATUS_INIT=0; //初始化状态
    public static int ORDER_STATUS_PAYED=1; //已支付
    public static int ORDER_STATUS_NOT_DISPATCHED = 2;
    public static int ORDER_STATUS_DISPATCHED = 3;
    public static int ORDER_STATUS_TRANSACTION_SUCCESS=4; //交易成功
    public static int ORDER_STATUS_TRANSACTION_CLOSE=5; //交易关闭
    public static int ORDER_STATUS_TRANSACTION_FAILED=6; //交易失败
    public static int ORDER_STATUS_TRANSACTION_CANCEL=7; //订单取消

    public static int ORDERITEM_STATUS_STOCK_LOCK = 1;
    public static int ORDERITEM_STATUS_STOCK_FREE = 2;
    public static int ORDERITEM_STATUS_STOCK_DEDUCTION = 3;

    public static final String PRODUCER_GROUP = "delay_order_cancel_producer";
    public static final String NAME_SERVER = "127.0.0.1:9876";
    public static final String DELAY_TOPIC = "delay_order_cancel";
    public static final int DELAY_LEVEL = 16;
//    messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    public static final String CONSUMER_GROUP = "delay_order_cancel_consumer";

}
