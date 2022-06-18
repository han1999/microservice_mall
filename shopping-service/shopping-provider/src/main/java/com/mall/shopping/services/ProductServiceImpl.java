package com.mall.shopping.services;

import com.mall.shopping.IProductService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.entitys.ItemDesc;
import com.mall.shopping.dal.persistence.ItemDescMapper;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.*;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import com.mall.shopping.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/

@Service
@Component
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Autowired
    private ProductConverter productConverter;

    @Override
    public ProductDetailResponse getProductDetail(ProductDetailRequest request) {
        ProductDetailResponse response = new ProductDetailResponse();
        try {
            request.requestCheck();
            Item item = itemMapper.selectByPrimaryKey(request.getId());
            if (item == null) {
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
            }
            /**
             * 这样感觉不太好，如果忘记写item.setImageBig的话， 这个属性就没有了
             */
            item.setImageBig();
            item.setImages();
            ProductDetailDto productDetailDto = productConverter.item2ProductDetailDto(item);
            ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(request.getId());
            //detail有可能就是null
            String detail = itemDesc.getItemDesc();
//            String detail = null;
            productDetailDto.setDetail(detail);
            response.setProductDetailDto(productDetailDto);
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("ProductServiceImpl.getProductDetail occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public AllProductResponse getAllProduct(AllProductRequest request) {
        return null;
    }

    @Override
    public RecommendResponse getRecommendGoods() {
        return null;
    }
}
