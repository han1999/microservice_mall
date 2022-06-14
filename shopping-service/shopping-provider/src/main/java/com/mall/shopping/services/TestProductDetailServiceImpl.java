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
    TestProductConverter testProductConverter;

    @Override
    public TestProductDetailDto getProductDetail(Long productId) {
        final Item item = itemMapper.selectByPrimaryKey(productId);

        TestProductDetailDto testProductDetailDto = testProductConverter.productDOToDTO(item);
        return testProductDetailDto;
    }
}
