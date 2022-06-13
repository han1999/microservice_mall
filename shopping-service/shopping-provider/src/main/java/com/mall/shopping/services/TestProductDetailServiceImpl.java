package com.mall.shopping.services;

import com.mall.shopping.ITestProductDetailService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.TestProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.TestProductDetailDto;
import com.mall.shopping.dto.TestProductDetailRequest;
import com.mall.shopping.dto.TestProductDetailResponse;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TestProductDetailServiceImpl implements ITestProductDetailService {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    TestProductConverter productConverter;

    @Override
    public TestProductDetailResponse getProductDetail(TestProductDetailRequest request) {
        System.out.println("调用了getProductDetail");
        TestProductDetailResponse response = new TestProductDetailResponse();
        try {
            // 进行参数校验
            request.requestCheck();
            Item item = itemMapper.selectByPrimaryKey(request.getProductId());
            TestProductDetailDto testProductDetailDto = productConverter.productDOToDTO(item);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
            response.setProductDetailDto(testProductDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(ShoppingRetCode.DB_EXCEPTION.getCode());
            response.setMsg(ShoppingRetCode.DB_EXCEPTION.getMessage());
        }

        return response;
    }
}
