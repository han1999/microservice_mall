
package com.mall.pay.biz.abs;

import com.mall.commons.result.AbstractRequest;
import com.mall.pay.utils.ParamValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证器基类
 * @author 
 */
public abstract class BaseValidator implements Validator {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void validate(AbstractRequest request) {
        //基本参数校验
        ParamValidatorUtils.validateV2(request);
        //特殊校验
//        specialValidate(request);
    }

    public abstract void specialValidate(AbstractRequest request);

    /**
     * 检验订单的公共方法
     * @param request
     * @param orderCoreService
     */
/*    public void commonValidate(AbstractRequest request, OrderCoreService orderCoreService) {
        if (request instanceof PaymentRequest) {
            PaymentRequest paymentRequest = (PaymentRequest) request;
            //校验订单是否存在
            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            orderDetailRequest.setOrderId(paymentRequest.getTradeNo());
            OrderDetailResponse orderDetailResponse = orderCoreService.getOrderDetail(orderDetailRequest);
            if (null == orderDetailResponse) {
                throw new BizException(PayReturnCodeEnum.NO_ORDER_NOT_EXIST.getCode(), PayReturnCodeEnum.NO_ORDER_NOT_EXIST.getMsg());
            }
            //校验订单是否已支付，同一订单支付幂等处理
            if (!Objects.equals(orderDetailResponse.getStatus(), 0)) {
                throw new BizException(PayReturnCodeEnum.HAD_PAY_ERROR.getCode(), PayReturnCodeEnum.HAD_PAY_ERROR.getMsg());
            }
            // 防止金额篡改等
            if(orderDetailResponse.getPayment().compareTo(paymentRequest.getOrderFee())!=0){
                throw new BizException(PayReturnCodeEnum.ORDER_AMOUNT_ERROR.getCode(), PayReturnCodeEnum.ORDER_AMOUNT_ERROR.getMsg());
            }
        }

    }*/
}
