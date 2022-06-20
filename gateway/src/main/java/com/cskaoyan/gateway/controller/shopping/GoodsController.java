package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IProductService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.AllProductRequest;
import com.mall.shopping.dto.AllProductResponse;
import com.mall.user.annotation.Anonymous;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@RestController
@RequestMapping("/shopping")
public class GoodsController {
    @Reference(timeout = 3000, retries = 0, check = false)
    IProductService productService;


    @GetMapping("/goods")
    @Anonymous
    public ResponseData goods(AllProductRequest request) {
//        request.set
        AllProductResponse response = productService.getAllProduct(request);
        if (ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            /**
             * response作为result 会多出来code和msg这两个无用信息
             */
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
}

}
