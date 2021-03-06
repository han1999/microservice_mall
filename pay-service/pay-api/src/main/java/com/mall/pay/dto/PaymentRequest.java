package com.mall.pay.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.validatorextend.PayChannel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;


@Data
public class PaymentRequest extends AbstractRequest{
    /**
     * 用户id
     */
    @NotNull(message = "userId不可为空")
    private Long userId;

    private String payerName;

    /**
     * 交易订单号, 统一生成全局唯一的订单号
     */
    @NotBlank(message = "tradeNo不可为空")
    private String tradeNo;

    /**
     * 实际支付金额(单位为分)
     */
    @Min(value = 0, message = "实际支付金额不能为负数")
    private BigDecimal totalFee;

    /**
     * 订单总金额
     */
    @Min(value = 0, message = "订单总金额不能为负数")
    private BigDecimal orderFee;

    /**
     * 商品描述(必填)
     * 微信支付提交格式要求；支付宝不需要严格控制格式
     * {浏览器打开的网站主页title名 -商品概述}
     */
    @NotBlank(message = "商品描述不能为空")
    private String subject;

    /**
     * 终端IP(非必填)
     */
    private String spbillCreateIp;

    /**
     * 支付渠道（alipay：支付宝  /  wechat_pay：微信）
     */
    @PayChannel
    private String payChannel;
    /**
     * 地址id
     */
    private Long addressId;

    @Override
    public void requestCheck() {
        if (userId==null || tradeNo==null) {
            throw new ValidateException(PayReturnCodeEnum.REQUISITE_PARAMETER_NOT_EXIST.getCode(), PayReturnCodeEnum.REQUISITE_PARAMETER_NOT_EXIST.getMsg());
        }
    }

    public String getSubject() {
        try {
            return new String(subject.getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
