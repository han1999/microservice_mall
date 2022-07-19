package com.cskaoyan.gateway.controller.promo;

import com.cskaoyan.gateway.cache.CacheManager;
import com.google.common.util.concurrent.RateLimiter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

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

    /*
    解决不好这里的cacheManager的注入问题
     */
//    CacheManager cacheManager;
//
//    @PostConstruct
//    public void init() {
//        cacheManager = new CacheManager();
//    }

    /**
     * 解决了， 不能实例化，因为缺少redis的配置文件，在yml加配置属性就可以了
     */

    @Autowired
    CacheManager cacheManager;

    RateLimiter rateLimiter;

    ExecutorService executorService;

    @PostConstruct
    public void init() {
        // 创建每秒有100个令牌的令牌桶
        rateLimiter = RateLimiter.create(100);

        //队列泄洪 利用固定数量的线程池来实现队列泄洪
        executorService = Executors.newFixedThreadPool(100);
    }

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
    public ResponseData seckill(@RequestBody CreatePromoOrderRequest request, HttpServletRequest httpServletRequest) {
        // 总体限流
        // 如果获取令牌失败，线程在这里阻塞等待一段时间
        rateLimiter.acquire();

        Long productId = request.getProductId();
        Long psId = request.getPsId();
        String notEnoughKey = cacheManager.checkCache("promo_order_stock_not_enough_" + productId + "_" + psId);
        if (notEnoughKey != null && "none".equals(notEnoughKey.trim())) {
            return new ResponseUtil<>().setErrorMsg("失败");
        }
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        request.setUserId(uid);

        Future<CreatePromoOrderResponse> future=executorService.submit(new Callable<CreatePromoOrderResponse>() {
            @Override
            public CreatePromoOrderResponse call() throws Exception {
                return promoService.createPromoOrderInTransaction(request);
            }
        });
        CreatePromoOrderResponse response = null;
        try {
            response = future.get();
//        CreatePromoOrderResponse response = promoService.createPromoOrderInTransaction(request);
            if (PromoRetCode.SUCCESS.getCode().equals(response.getCode())) {
                return new ResponseUtil<>().setData(response.getProductId());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @GetMapping("/comment/count")
    public ResponseData count(Long productId) {
        return new ResponseUtil<>().setData(10);
    }


}
