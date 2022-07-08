package com.mall.shopping.services;

import com.mall.shopping.IProductCateService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ProductCateConverter;
import com.mall.shopping.dal.entitys.ItemCat;
import com.mall.shopping.dal.persistence.ItemCatMapper;
import com.mall.shopping.dto.AllProductCateRequest;
import com.mall.shopping.dto.AllProductCateResponse;
import com.mall.shopping.dto.ProductCateDto;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import com.mall.shopping.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/

@Service
@Component
@Slf4j
public class ProductCateServiceImpl implements IProductCateService {
    @Autowired
    ItemCatMapper itemCatMapper;

    @Autowired
    ProductCateConverter productCateConverter;

    @Override
    public AllProductCateResponse getAllProductCate(AllProductCateRequest request) {
        return null;
    }

    @Override
    public AllProductCateResponse getAllProductCate() {
        AllProductCateResponse response = new AllProductCateResponse();
        try {
//            List<ItemCat> itemCatList = itemCatMapper.selectAll();
            //上面的selectAll满足要求，但理论上说应该有筛选条件的
            //实际上加了setOrder后没有影响
            Example example = new Example(ItemCat.class);
            example.createCriteria().andEqualTo("status", 1);
            example.setOrderByClause("sort_order");
            List<ItemCat> itemCatList = itemCatMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(itemCatList)) {
                List<ProductCateDto> productCateDtoList = productCateConverter.items2Dto(itemCatList);
                response.setProductCateDtoList(productCateDtoList);
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("ProductCateServiceImpl.getAllProductCate occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
