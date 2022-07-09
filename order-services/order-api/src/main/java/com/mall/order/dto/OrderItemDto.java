package com.mall.order.dto;/**
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@Data
public class OrderItemDto implements Serializable{
    private String id;

    private String itemId;

    private String orderId;

    private Integer num;

    private String title;

    private BigDecimal price;

    private BigDecimal totalFee;

    /**
     * 原本少了一个orderStatus 程序也能运行，但是可能后面会出现问题，所以加了一个orderStatus
     */
    private Integer orderStatus;

    private String picPath;
}
