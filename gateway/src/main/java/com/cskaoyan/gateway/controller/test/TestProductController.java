package com.cskaoyan.gateway.controller.test;


import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.ITestProductDetailService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.TestProductDetailDto;
import com.mall.shopping.dto.TestProductDetailRequest;
import com.mall.shopping.dto.TestProductDetailResponse;
import com.mall.user.annotation.Anoymous;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestProductController {

    @Reference(timeout = 3000, retries = 0, check = false)
    ITestProductDetailService iTestProductDetailService;



    @GetMapping("/product")
    @Anoymous
    public ResponseData getProductDetail(Long productId) {

        TestProductDetailRequest request = new TestProductDetailRequest();
        request.setProductId(productId);

        TestProductDetailResponse productDetail = iTestProductDetailService.getProductDetail(request);
        if (ShoppingRetCode.SUCCESS.getCode().equals(productDetail.getCode())) {
            return new ResponseUtil().setData(productDetail.getProductDetailDto());
        }

        return new ResponseUtil().setErrorMsg(productDetail.getMsg());
    }
}
