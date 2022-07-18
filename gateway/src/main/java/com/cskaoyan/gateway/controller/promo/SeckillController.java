package com.cskaoyan.gateway.controller.promo;

import com.mall.commons.lock.DistributedLockException;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.RequestUtils;
import com.mall.promo.PromoService;
import com.mall.promo.constant.PromoRetCode;
import com.mall.promo.dto.*;
import com.mall.user.annotation.Anonymous;
import com.mall.user.intercepter.TokenIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/17
 **/

@RestController
@Slf4j
@RequestMapping("/shopping")
public class SeckillController {

    @Reference(timeout = 3000, retries = 0, check = false)
    PromoService promoService;

    @GetMapping("seckilllist")
    @Anonymous
    public ResponseData seckillList(int sessionId) {
        PromoInfoRequest request = new PromoInfoRequest();
        request.setSessionId(sessionId);
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        request.setYyyymmdd(yyyyMMdd);
        PromoInfoResponse response = promoService.getPromoList(request);
        if (PromoRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @PostMapping("/promoProductDetail")
    public ResponseData promoProductDetail(@RequestBody PromoProductDetailRequest request) {
        PromoProductDetailResponse response = promoService.getPromoProductDetail(request);
        if (PromoRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @PostMapping("seckill")
    public ResponseData seckill(@RequestBody CreatePromoOrderRequest request, HttpServletRequest httpServletRequest) throws DistributedLockException {
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        request.setUserId(uid);
        CreatePromoOrderResponse response = promoService.createPromoOrderInTransaction(request);
        if (PromoRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getProductId());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @GetMapping("/comment/count")
    public ResponseData count(Long productId) {
        return new ResponseUtil<>().setData(10);
    }


}
