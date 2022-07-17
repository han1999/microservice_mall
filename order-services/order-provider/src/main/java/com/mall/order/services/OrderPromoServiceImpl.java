package com.mall.order.services;

import com.mall.order.OrderPromoService;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.handler.InitOrderHandler;
import com.mall.order.biz.handler.LogisticalHandler;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.CartProductDto;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
import com.mall.order.utils.ResponseUtils;
import com.mall.shopping.IProductService;
import com.mall.shopping.dto.ProductDetailDto;
import com.mall.shopping.dto.ProductDetailRequest;
import com.mall.shopping.dto.ProductDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/17
 **/

@Component
@Slf4j
@Service
public class OrderPromoServiceImpl implements OrderPromoService {
    @Reference(timeout = 3000, retries = 0, check = false)
    IProductService productService;

    @Autowired
    InitOrderHandler initOrderHandler;

    @Autowired
    LogisticalHandler logisticalHandler;

    @Override
    @Transactional
    public CreateSeckillOrderResponse createPromoOrder(CreateSeckillOrderRequest request) {
        CreateSeckillOrderResponse response = new CreateSeckillOrderResponse();
        try {
            request.requestCheck();
            CreateOrderContext createOrderContext = new CreateOrderContext();
            //填充必要参数，生成订单
            inflateOrderParam(request, createOrderContext);
            boolean isSuccess = initOrderHandler.handle(createOrderContext);

            //物流信息
            createOrderContext.setTel(request.getTel());
            createOrderContext.setStreetName(request.getStreetName());
            logisticalHandler.handle(createOrderContext);

            log.info("秒杀订单，物流信息插入数据库: {}", isSuccess);

            if (isSuccess) {
                return ResponseUtils.setCodeAndMsg(response, OrderRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, OrderRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("OrderPromoServiceImpl.createPromoOrder occurs Exception :" + e);

        }
        return response;
    }

    private void inflateOrderParam(CreateSeckillOrderRequest request, CreateOrderContext createOrderContext) {
        List<CartProductDto> list = getCartProductList(request);

        //构建下单参数
        createOrderContext.setUserId(request.getUserId());
        createOrderContext.setBuyerNickName(request.getUsername());
        createOrderContext.setAddressId(request.getAddressId());
        createOrderContext.setCartProductDtoList(list);
        createOrderContext.setOrderTotal(request.getPrice());
    }

    private List<CartProductDto> getCartProductList(CreateSeckillOrderRequest request) {
        //构建购物车列表
        ProductDetailDto productDetailDto = getProductDetail(request);
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setProductId(request.getProductId());
        cartProductDto.setProductImg(productDetailDto.getProductImageBig());

        //秒杀商品一次买一个
        cartProductDto.setProductNum(1L);
        cartProductDto.setProductName(productDetailDto.getProductName());
        cartProductDto.setSalePrice(request.getPrice());

        ArrayList<CartProductDto> cartProductDtos = new ArrayList<>();
        cartProductDtos.add(cartProductDto);
        return cartProductDtos;
    }

    private ProductDetailDto getProductDetail(CreateSeckillOrderRequest request) {
        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
        productDetailRequest.setId(request.getProductId());
        ProductDetailResponse productDetail = productService.getProductDetail(productDetailRequest);
        return productDetail.getProductDetailDto();
    }
}
