package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IProductCateService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.AllProductCateResponse;
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
public class CategoriesController {
    @Reference(timeout = 3000, retries = 0, check = false)
    IProductCateService productCateService;

    @GetMapping("/categories")
    @Anonymous
    public ResponseData categories() {
//        AllProductCateRequest request = new AllProductCateRequest();
//        //我不知道这个sort有什么用
//        request.setSort("id");
        AllProductCateResponse response = productCateService.getAllProductCate();
        if (ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getProductCateDtoList());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }


}
