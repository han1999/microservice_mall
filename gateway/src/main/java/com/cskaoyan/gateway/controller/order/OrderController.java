package com.cskaoyan.gateway.controller.order;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.RequestUtils;
import com.mall.order.OrderCoreService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.*;
import com.mall.user.intercepter.TokenIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/9
 **/

@RestController
@RequestMapping("/shopping")
@Slf4j
public class OrderController {

    @Reference(timeout = 3000, retries = 0, check = false)
    OrderCoreService orderCoreService;

    @PostMapping("/order")
    public ResponseData createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpServletRequest) {
        /**
         * CreateOrderRequest的orderTotal是BigDecimal类型， 不知道能不能正确传入
         * 可以正确传入！
         */
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        request.setUserId(uid);
        BigDecimal orderTotal = request.getOrderTotal();
//        System.out.println("orderTotal = " + orderTotal);
        log.debug("orderTotal={}", orderTotal);
        CreateOrderResponse response = orderCoreService.createOrder(request);
        if (OrderRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getOrderId());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @GetMapping("/order")
    public ResponseData getAllOrders(OrderListRequest request, HttpServletRequest httpServletRequest) {
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        request.setUserId(uid);
        OrderListResponse response = orderCoreService.getAllOrders(request);
        if (OrderRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    /**
     * id 虽然是数字的形式，但是应该也可以被识别为字符串
     * @param orderId
     * @return
     */
    @GetMapping("/order/{id}")
    public ResponseData getOrderDetail(@PathVariable("id") String orderId) {
        OrderDetailRequest request = new OrderDetailRequest();
        request.setOrderId(orderId);
        OrderDetailResponse response = orderCoreService.getOrderDetail(request);
        if (OrderRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
