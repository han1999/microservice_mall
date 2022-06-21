package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.RequestUtils;
import com.mall.shopping.ICartService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.AddCartRequest;
import com.mall.shopping.dto.AddCartResponse;
import com.mall.shopping.dto.CartListByIdRequest;
import com.mall.shopping.dto.CartListByIdResponse;
import com.mall.user.intercepter.TokenIntercepter;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/21
 **/

@RestController
@RequestMapping("/shopping")
public class CartsController {

    @Reference(timeout = 3000, retries = 0, check = false)
    ICartService cartService;

    @GetMapping("/carts")
    public ResponseData getCartListById(HttpServletRequest httpServletRequest) {
//        String userInfo = (String) httpServletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
//        JSONObject jsonObject = JSON.parseObject(userInfo);
//        Long uid = jsonObject.getObject("uid", Long.class);
        Long uid = RequestUtils.getStringAttributeJsonValue(httpServletRequest, TokenIntercepter.USER_INFO_KEY, "uid", Long.class);
        CartListByIdRequest request = new CartListByIdRequest();
        request.setUserId(uid);
        CartListByIdResponse response = cartService.getCartListById(request);
        if (ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getCartProductDtos());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    @PostMapping("/carts")
    public ResponseData addToCart(@RequestBody Map<String, Long> map) {
        AddCartRequest request = new AddCartRequest();
        /**
         * userId 对应的json格式是 String 类型，但是Json转化工具应该可以将其转为Long
         * 是的，没错！
         */
        request.setUserId(map.get("userId"));
        request.setItemId(map.get("productId"));
        //intValue返回的是int类型，但是有自动装箱机制,会变成Integer
        request.setNum(map.get("productNum").intValue());
        AddCartResponse response = cartService.addToCart(request);
        if (ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getMsg());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());

    }

}
