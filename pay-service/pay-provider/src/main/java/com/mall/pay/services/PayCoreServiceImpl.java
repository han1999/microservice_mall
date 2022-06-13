package com.mall.pay.services;

import com.mall.order.OrderCoreService;
import com.mall.order.OrderQueryService;
import com.mall.order.constant.OrderConstants;
import com.mall.order.dto.PayOrderSuccessRequest;
import com.mall.order.dto.PayOrderSuccessResponse;
import com.mall.pay.PayCoreService;
import com.mall.pay.biz.payment.constants.PayResultEnum;
import com.mall.pay.biz.payment.constants.PaymentConstants;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dal.entitys.Payment;
import com.mall.pay.dal.persistence.PaymentMapper;
import com.mall.pay.dto.PaymentRequest;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.pay.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Slf4j
@Service(cluster = "failover",timeout = 2000)
public class PayCoreServiceImpl implements PayCoreService {

    @Autowired
    PayHelper helper;

    @Autowired
    PaymentMapper paymentMapper;

    @Reference(check = false)
    OrderCoreService orderCoreService;

    @Reference
    OrderQueryService orderQueryService;

    @Override
    public AlipaymentResponse aliPay(PaymentRequest request) {
        request.requestCheck();

        AlipaymentResponse alipaymentResponse = new AlipaymentResponse();
        alipaymentResponse.setCode(PayReturnCodeEnum.SUCCESS.getCode());
        alipaymentResponse.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());

        String code = helper.getCode(request);
        if (code == null) {
          // 获取二维码失败
            alipaymentResponse.setCode(PayReturnCodeEnum.GET_CODE_FALIED.getCode());
            alipaymentResponse.setMsg(PayReturnCodeEnum.GET_CODE_FALIED.getMsg());
            return alipaymentResponse;
        }

        Payment payment = generatePayment(request);
        alipaymentResponse.setQrCode(code);
        try {
            paymentMapper.insert(payment);
        } catch (Exception e) {
            log.error("PayCoreServiceImpl.aliPay occurs error: " + e);
            ExceptionProcessorUtils.wrapperHandlerException(alipaymentResponse, e);
        }
        return alipaymentResponse;

    }

    private Payment generatePayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setCreateTime(new Date());
        payment.setOrderId(request.getTradeNo());
        BigDecimal orderFee = request.getOrderFee();
        payment.setOrderAmount(orderFee);
        payment.setPayerAmount(orderFee);
        payment.setPayWay("支付宝");
        payment.setProductName(request.getSubject());
        payment.setPayWay(request.getPayChannel());
        payment.setPayerUid(request.getUserId());
        payment.setPayerName("徐凤年");
        payment.setStatus(PaymentConstants.PayStatusEnum.INIT_STATUS.getStatus() + "");
        payment.setRemark("支付宝支付");
        payment.setUpdateTime(new Date());
        return payment;
    }

    @Override
    public AlipayQueryRetResponse queryAlipayRet(PaymentRequest request) {

        AlipayQueryRetResponse alipayQueryRetResponse = new AlipayQueryRetResponse();
        alipayQueryRetResponse.setCode(PayReturnCodeEnum.SUCCESS.getCode());
        alipayQueryRetResponse.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());


        try {
            request.requestCheck();
            Example e1 = new Example(Payment.class);
            e1.createCriteria().andEqualTo("orderId", request.getTradeNo());
            List<Payment> payments = paymentMapper.selectByExample(e1);
            Payment payment1 = payments.get(0);

            if (PaymentConstants.PayStatusEnum.PAY_SUCCESS.getStatus().toString().equals(payment1.getStatus())) {
                return alipayQueryRetResponse;
            }
            helper.queryTrade(request, payStatus -> {
                Example example = new Example(Payment.class);
                example.createCriteria().andEqualTo("orderId", request.getTradeNo());
                Payment payment = new Payment();
                payment.setUpdateTime(new Date());
                if (payStatus) {
                    payment.setStatus(PaymentConstants.PayStatusEnum.PAY_SUCCESS.getStatus().toString());
                    payment.setPaySuccessTime(new Date());
                    payment.setCompleteTime(new Date());

                    PayOrderSuccessRequest payOrderSuccessRequest = new PayOrderSuccessRequest();
                    payOrderSuccessRequest.setOrderId(request.getTradeNo());
                    PayOrderSuccessResponse payOrderSuccessResponse
                            = orderCoreService.payOrderSuccess(payOrderSuccessRequest);
                    alipayQueryRetResponse.setCode(payOrderSuccessResponse.getCode());
                    alipayQueryRetResponse.setMsg(payOrderSuccessResponse.getMsg());
                } else {
                    payment.setStatus(PaymentConstants.PayStatusEnum.PAY_FAILED.getStatus().toString());
                    payment.setCompleteTime(new Date());
                    //orderCoreService.updateOrder(OrderConstants.ORDER_STATUS_TRANSACTION_FAILED, request.getTradeNo());

                    alipayQueryRetResponse.setCode(PayReturnCodeEnum.ORDER_HAD_NOT_PAY.getCode());
                    alipayQueryRetResponse.setCode(PayReturnCodeEnum.ORDER_HAD_NOT_PAY.getMsg());
                }

                paymentMapper.updateByExampleSelective(payment,example);
            });
        } catch (Exception e) {
            log.error("PayCoreServiceImpl.queryAlipayRet occurs error: " + e);
            ExceptionProcessorUtils.wrapperHandlerException(alipayQueryRetResponse, e);
        }
        return alipayQueryRetResponse;
    }
}
