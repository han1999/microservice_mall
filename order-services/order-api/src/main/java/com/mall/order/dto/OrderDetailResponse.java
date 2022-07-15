package com.mall.order.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class OrderDetailResponse extends AbstractResponse{

    /**
     * 应该将data再封装到一个类中，但是我懒的做了
     */
    private String userName;
    private Long orderTotal;
    private Long userId;
    private List<OrderItemDto> goodsList;
    private String tel;
    private String streetName;
/**
 * 写的什么狗屁东西，混淆视听
 */
//    private String orderId;
//
//    private BigDecimal payment;
//
//    private Integer paymentType;
//
//    private BigDecimal postFee;
//
//    private Integer status;
//
//    private Date createTime;
//
//    private Date updateTime;
//
//    private Date paymentTime;
//
//    private Date consignTime;
//
//    private Date endTime;
//
//    private Date closeTime;
//
//    private String shippingName;
//
//    private String shippingCode;
//
//    private Long userId;
//
//    private String buyerMessage;
//
//    private String buyerNick;
//
//    private Integer buyerComment;
//
//    private List<OrderItemDto> orderItemDto;
//
//    private OrderShippingDto orderShippingDto;
}
