package com.mall.shopping.services;

import com.mall.shopping.ITestProductDetailService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.TestProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.TestProductDetailDto;
import com.mall.shopping.dto.TestProductDetailRequest;
import com.mall.shopping.dto.TestProductDetailResponse;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TestProductDetailServiceImpl implements ITestProductDetailService {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    TestProductConverter testProductConverter;

//    @Override
    public TestProductDetailDto getProductDetail(Long productId) {
        TestProductDetailDto testProductDetailDto = null;
        try {
            final Item item = itemMapper.selectByPrimaryKey(productId);
            testProductDetailDto = testProductConverter.productDOToDTO(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testProductDetailDto;
    }

    @Override
    public TestProductDetailResponse getProductDetail(TestProductDetailRequest request) {

        TestProductDetailResponse response = new TestProductDetailResponse();
        try {
            request.requestCheck();
            final Item item = itemMapper.selectByPrimaryKey(request.getProductId());
            TestProductDetailDto testProductDetailDto = testProductConverter.productDOToDTO(item);
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
            response.setTestProductDetailDto(testProductDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(ShoppingRetCode.DB_EXCEPTION.getCode());
            response.setMsg(ShoppingRetCode.DB_EXCEPTION.getMessage());
        }
        return response;
    }
}
