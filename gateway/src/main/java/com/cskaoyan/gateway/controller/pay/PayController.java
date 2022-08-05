package com.cskaoyan.gateway.controller.pay;

import com.cskaoyan.gateway.form.pay.PayForm;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.RequestUtils;
import com.mall.pay.PayCoreService;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dto.PaymentRequest;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.user.intercepter.TokenIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/15
 **/

@RestController
@RequestMapping("/cashier")
@Slf4j
public class PayController {

    @Reference(timeout = 3000, retries = 0, check = false)
    PayCoreService payCoreService;

    /**
     * 应该是取自application.yml
     */
    @Value("${server.port}")
    String port;

    @Value("${server.ip}")
    String serverIp;

    @PostMapping("/pay")
    public ResponseData pay(@RequestBody PayForm payForm, HttpServletRequest httpServletRequest) {
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        PaymentRequest request = new PaymentRequest();
        request.setUserId(uid);
        request.setTotalFee(payForm.getMoney());
        request.setOrderFee(payForm.getMoney());
        request.setTradeNo(payForm.getOrderId());
        request.setPayChannel(payForm.getPayType());
        request.setPayerName(payForm.getNickName());
        request.setSubject(payForm.getInfo());
        AlipaymentResponse response = payCoreService.aliPay(request);
        if (PayReturnCodeEnum.SUCCESS.getCode().equals(response.getCode())) {
            String qrCode = response.getQrCode();
            log.info("port:{}", port);
            String codeUrl = "http://"+serverIp+":" + port + "/image/" + qrCode;
            log.info("codeUrl:{}", codeUrl);
            return new ResponseUtil<>().setData(codeUrl);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @GetMapping("/queryStatus")
    public ResponseData queryStatus(String orderId, HttpServletRequest httpServletRequest) {
        PaymentRequest request = new PaymentRequest();
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        request.setUserId(uid);
        request.setTradeNo(orderId);
        AlipayQueryRetResponse response = payCoreService.queryAlipayRet(request);
        if (PayReturnCodeEnum.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getMsg());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        System.out.println("property = " + property);
    }
}
