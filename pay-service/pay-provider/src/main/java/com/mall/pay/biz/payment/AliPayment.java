package com.mall.pay.biz.payment;//package com.mall.pay.biz.payment;
//
//import com.alibaba.fastjson.JSON;
//import com.mall.commons.result.AbstractRequest;
//import com.mall.commons.result.AbstractResponse;
//import com.mall.commons.tool.exception.BizException;
//import com.mall.order.OrderCoreService;
//import com.mall.pay.biz.abs.*;
//import com.mall.pay.biz.payment.channel.alipay.AlipayBuildRequest;
//import com.mall.pay.biz.payment.channel.alipay.AlipayNotify;
//import com.mall.pay.biz.payment.constants.AliPaymentConfig;
//import com.mall.pay.biz.payment.constants.PayResultEnum;
//import com.mall.pay.biz.payment.context.AliPaymentContext;
//import com.mall.pay.dal.entitys.Payment;
//import com.mall.pay.dal.persistence.PaymentMapper;
//import com.mall.pay.constants.PayChannelEnum;
//import com.mall.pay.constants.PayReturnCodeEnum;
//import com.mall.pay.dto.PaymentNotifyRequest;
//import com.mall.pay.dto.PaymentNotifyResponse;
//import com.mall.pay.dto.PaymentRequest;
//import com.mall.pay.dto.PaymentResponse;
//import com.mall.pay.dto.alipay.AlipaymentResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import tk.mybatis.mapper.entity.Example;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.util.*;
//
//
//@Slf4j
//@Service("aliPayment")
//public class AliPayment {
//
//	@Resource(name = "aliPaymentValidator")
//	private Validator validator;
//
//	@Autowired
//	AliPaymentConfig aliPaymentConfig;
//
//	@Autowired
//	PaymentMapper paymentMapper;
//
//	@Reference(timeout = 30000,check = false)
//	OrderCoreService orderCoreService;
//
//
//
//	public AliPaymentContext createContext(AbstractRequest request) {
//		AliPaymentContext aliPaymentContext = new AliPaymentContext();
//		PaymentRequest paymentRequest = (PaymentRequest) request;
//		aliPaymentContext.setSubject(paymentRequest.getSubject());
//		aliPaymentContext.setOutTradeNo(paymentRequest.getTradeNo());
//		aliPaymentContext.setTotalFee(paymentRequest.getTotalFee());
//		aliPaymentContext.setUserId(paymentRequest.getUserId());
//		return aliPaymentContext;
//	}
//
//	public void prepare(AliPaymentContext context) throws BizException {
//		SortedMap sParaTemp = context.getsParaTemp();
//		AliPaymentContext aliPaymentContext = (AliPaymentContext) context;
//		sParaTemp.put("partner", aliPaymentConfig.getAli_partner());
//		sParaTemp.put("service", aliPaymentConfig.getAli_service());
//		//sParaTemp.put("seller_email", aliPaymentConfig.getSeller_email());
//		sParaTemp.put("seller_id", aliPaymentConfig.getSeller_id());
//		sParaTemp.put("payment_type", "1");
//		sParaTemp.put("it_b_pay", aliPaymentConfig.getIt_b_pay());
//		sParaTemp.put("notify_url", aliPaymentConfig.getNotify_url());
//		sParaTemp.put("return_url", aliPaymentConfig.getReturn_url());
//		sParaTemp.put("out_trade_no", aliPaymentContext.getOutTradeNo());
//		sParaTemp.put("subject", aliPaymentContext.getSubject());
//		sParaTemp.put("total_fee", aliPaymentContext.getTotalFee());
//		aliPaymentContext.setsParaTemp(sParaTemp);
//	}
//
//
//	public AlipaymentResponse generalProcess(AliPaymentContext context) throws BizException {
//		Map<String, Object> sPara = AlipayBuildRequest.buildRequestParam(context.getsParaTemp(), aliPaymentConfig);
//		log.info("?????????????????????????????????:{}", JSON.toJSONString(sPara));
//		String strPara = AlipayBuildRequest.buildRequest(sPara, "get", "??????", aliPaymentConfig);
//		log.info("????????????????????????????????????:{}",strPara);
//		AlipaymentResponse response = new AlipaymentResponse();
//		response.setCode(PayReturnCodeEnum.SUCCESS.getQrCode());
//		response.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());
//		response.setQrCode(strPara);
//		return response;
//	}
//
//
//	public void afterProcess(AbstractRequest request, AbstractResponse respond, Context context) throws BizException {
//		log.info("Alipayment begin - afterProcess -request:" + request + "\n response:" + respond);
//		PaymentRequest paymentRequest = (PaymentRequest) request;
//		//?????????????????????
//		Payment payment = new Payment();
//		payment.setCreateTime(new Date());
//		//?????????
//		payment.setOrderId(paymentRequest.getTradeNo());
//		payment.setCreateTime(new Date());
//		BigDecimal amount =paymentRequest.getOrderFee();
//		payment.setOrderAmount(amount);
//		payment.setPayerAmount(amount);
//		payment.setPayerUid(paymentRequest.getUserId());
//		payment.setPayerName("");//TODO
//		payment.setPayWay(paymentRequest.getPayChannel());
//		payment.setProductName(paymentRequest.getSubject());
//		payment.setStatus(PayResultEnum.TRADE_PROCESSING.getQrCode());//
//		payment.setRemark("???????????????");
//		payment.setUpdateTime(new Date());
//		paymentMapper.insert(payment);
//	}
//
//
//	/**
//	 * ??????????????????
//	 * @param request
//	 * @return
//	 * @throws BizException
//	 */
//	public AbstractResponse completePayment(PaymentNotifyRequest request) {
//		request.requestCheck();
//		Map requestParams = request.getResultMap();
//		Map<String, Object> params = new HashMap<>(requestParams.size());
//		requestParams.forEach((key, value) -> {
//			String[] values = (String[]) value;
//			params.put((String) key, StringUtils.join(values, ","));
//		});
//
//		PaymentNotifyResponse response = new PaymentNotifyResponse();
//		String orderId = params.get("out_trade_no").toString();
//		//??????
//		if (AlipayNotify.verify(params, aliPaymentConfig)) {
//			Payment payment = new Payment();
//			//TRADE_FINISH(????????????)???TRADE_SUCCESS(????????????)???FAIL(????????????)
//			String tradeStatus = params.get("trade_status").toString();
//			if ("TRADE_SUCCESS".equals(tradeStatus)) {
//				//???????????????
//				payment.setPayNo(params.get("trade_no").toString());
//				payment.setStatus(PayResultEnum.TRADE_SUCCESS.getQrCode());
//				payment.setPaySuccessTime((Date) params.get("gmt_payment"));
//				Example example=new Example(Payment.class);
//				example.createCriteria().andEqualTo("orderId",orderId);
//				paymentMapper.updateByExampleSelective(payment,example);
//				//?????????????????????
//				orderCoreService.updateOrder(1, orderId);
//				response.setResult("success");
//				return response;
//			} else if ("TRADE_FINISH".equals(tradeStatus)) {
//				payment.setStatus(PayResultEnum.TRADE_FINISHED.getQrCode());
//				paymentMapper.updateByExampleSelective(payment, orderId);
//				//?????????????????????
//				orderCoreService.updateOrder(1, orderId);
//				response.setResult("success");
//			} else if ("FAIL".equals(tradeStatus)) {
//				payment.setStatus(PayResultEnum.FAIL.getQrCode());
//				paymentMapper.updateByExampleSelective(payment, orderId);
//				response.setResult("success");
//			} else {
//				response.setResult("fail");
//			}
//		} else {
//			throw new BizException("???????????????????????????");
//		}
//		return response;
//	}
//}
