package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IProductService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.RecommendResponse;
import com.mall.user.annotation.Anonymous;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/21
 **/
@RestController
@RequestMapping("/shopping")
public class RecommendController {

    @Reference(timeout = 3000, retries = 0, check = false)
    IProductService productService;


    @GetMapping("/recommend")
    @Anonymous
    public ResponseData recommend() {
        RecommendResponse response = productService.getRecommendGoods();
        if (ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getPanelDtoList());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }


}
