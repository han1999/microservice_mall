package com.mall.order.biz.handler;

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.shopping.ICartService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.dto.ClearCartItemRequest;
import com.mall.shopping.dto.ClearCartItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 将购物车中的缓存失效
 */
@Slf4j
@Component
public class ClearCartItemHandler extends AbstractTransHandler {

    @Reference(timeout = 3000, retries = 0, check = false)
    ICartService cartService;
    //是否采用异步方式执行
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Transactional
    public boolean handle(TransHandlerContext context) {
        CreateOrderContext createOrderContext = (CreateOrderContext) context;
        ClearCartItemRequest request = new ClearCartItemRequest();
        request.setUserId(createOrderContext.getUserId());
        request.setProductIds(createOrderContext.getBuyProductIds());
        ClearCartItemResponse response = cartService.clearCartItemByUserID(request);
        if (!ShoppingRetCode.SUCCESS.getCode().equals(response.getCode())) {
            throw new BizException(response.getCode(), response.getMsg());
        }
        return true;
    }
}
