package com.cskaoyan.gateway.controller.test;


import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.ITestProductDetailService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.TestProductDetailRequest;
import com.mall.shopping.dto.TestProductDetailResponse;
import com.mall.user.annotation.Anoymous;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
public class TestProductController {

    @Reference(timeout = 3000, retries = 0, check = false)
    ITestProductDetailService iTestProductDetailService;

//    @GetMapping("product")
//    @Anoymous
//    public ResponseData getProductDetail(Long productId) {
//        TestProductDetailDto productDetail = iTestProductDetailService.getProductDetail(productId);
//        ResponseData responseData = new ResponseData();
//        responseData.setCode(200);
//        responseData.setResult(productDetail);
//        responseData.setMessage("获取商品信息成功");
//        responseData.setSuccess(true);
//        return responseData;
//    }

    @GetMapping("product")
    @Anoymous
    public ResponseData getProductDetail(Long productId) {
        TestProductDetailRequest request = new TestProductDetailRequest();
        request.setProductId(productId);

        TestProductDetailResponse productDetail = iTestProductDetailService.getProductDetail(request);
        if (ShoppingRetCode.SUCCESS.equals(productDetail.getCode())) {
            return new ResponseUtil().setData(productDetail.getTestProductDetailDto());
        }
        return new ResponseUtil().setErrorMsg(productDetail.getMsg());
    }

}
