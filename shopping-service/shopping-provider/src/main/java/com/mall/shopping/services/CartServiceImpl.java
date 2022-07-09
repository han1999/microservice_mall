package com.mall.shopping.services;

import com.mall.shopping.ICartService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.CartItemConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.*;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import com.mall.shopping.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/21
 **/

@Service
@Component
@Slf4j
public class CartServiceImpl implements ICartService {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ItemMapper itemMapper;

    public CartListByIdResponse getCartListById(CartListByIdRequest request) {
        CartListByIdResponse response = new CartListByIdResponse();
        try {
            request.requestCheck();
            /**
             * 这个接口就算没有实现，如果购物车里面有商品，那么页面仍能正常显示
             * 如果购物车是空的，那么页面显示不了了
             */
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            log.info(request.getUserId() + "对应的hash是否存在：" + userCartMap.isExists());
            ArrayList<CartProductDto> cartProductDtos = new ArrayList<>();
            /**
             * redisson的机制：userCartMap不为null, 可以通过RMap.isExists()来判断对应的hash是否存在
             */
            for (CartProductDto cartProductDto : userCartMap.values()) {
                cartProductDtos.add(cartProductDto);
            }
            response.setCartProductDtos(cartProductDtos);
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("CartServiceImpl.getCartListById occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public AddCartResponse addToCart(AddCartRequest request) {
        AddCartResponse response = new AddCartResponse();
        try {
            request.requestCheck();
            /**
             * Long --> productId
             * CartProductDto --> productId 对应的 dto
             * getMap如果没有查到key对应的hash，userCartMap也不是null
             */
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            Long itemId = request.getItemId();
            CartProductDto cartProductDto = userCartMap.get(itemId);
            if (cartProductDto == null) {
                /**
                 * 该用户没有将这个商品加入购物车过
                 */
                Item item = itemMapper.selectByPrimaryKey(itemId);
                cartProductDto = CartItemConverter.item2Dto(item);
                cartProductDto.setProductNum(request.getNum().longValue());
                cartProductDto.setChecked("true");
                userCartMap.put(itemId, cartProductDto);
            } else {
                /**
                 * cartProductDto.getProductNum()+request.getNum()
                 * 是Long+Integer， 自动转换成Long了
                 */
                cartProductDto.setProductNum(cartProductDto.getProductNum() + request.getNum());
                userCartMap.put(itemId, cartProductDto);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("CartServiceImpl.addToCart occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public UpdateCartNumResponse updateCartNum(UpdateCartNumRequest request) {
        UpdateCartNumResponse response = new UpdateCartNumResponse();
        try {
            request.requestCheck();
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            Long itemId = request.getItemId();
            CartProductDto cartProductDto = userCartMap.get(itemId);
            if (cartProductDto != null) {
                cartProductDto.setProductNum(request.getNum().longValue());
                cartProductDto.setChecked(request.getChecked());
                userCartMap.put(itemId, cartProductDto);
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("CartServiceImpl.updateCartNum occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public CheckAllItemResponse checkAllCartItem(CheckAllItemRequest request) {
        CheckAllItemResponse response = new CheckAllItemResponse();
        try {
            request.requestCheck();
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            Set<Map.Entry<Long, CartProductDto>> entries = userCartMap.entrySet();
            for (Map.Entry<Long, CartProductDto> entry : entries) {
                CartProductDto cartProductDto = entry.getValue();
                cartProductDto.setChecked(request.getChecked());
                userCartMap.put(entry.getKey(), cartProductDto);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("CartServiceImpl.checkAllCartItem occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public DeleteCartItemResponse deleteCartItem(DeleteCartItemRequest request) {
        DeleteCartItemResponse response = new DeleteCartItemResponse();
        try {
            request.requestCheck();
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            Long itemId = request.getItemId();
            /**
             * 对 userCartMap 的操作就相当于对 redis的操作，不需要有一个flush过程(应该是自动flush了)
             */
            userCartMap.remove(itemId);
            if (userCartMap.get(itemId) == null) {
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("CartServiceImpl.deleteCartItem occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public DeleteCheckedItemResposne deleteCheckedItem(DeleteCheckedItemRequest request) {
        DeleteCheckedItemResposne resposne = new DeleteCheckedItemResposne();
        try {
            request.requestCheck();
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(request.getUserId().toString());
            Set<Long> keySet = userCartMap.keySet();
            Iterator<Long> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                Long key = iterator.next();
                CartProductDto cartProductDto = userCartMap.get(key);
                if ("true".equals(cartProductDto.getChecked())) {
                    userCartMap.remove(key);
                }
            }
            return ResponseUtils.setCodeAndMsg(resposne, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("CartServiceImpl.deleteCheckedItem occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(resposne, e);
        }
        return resposne;
    }

    /**
     * 这个接口，是为了订单业务准备的
     * @param request
     * @return
     */
    @Override
    public ClearCartItemResponse clearCartItemByUserID(ClearCartItemRequest request) {
        ClearCartItemResponse response = new ClearCartItemResponse();
        try {
            request.requestCheck();
            Long userId = request.getUserId();
            RMap<Long, CartProductDto> userCartMap = redissonClient.getMap(userId.toString());
            List<Long> productIds = request.getProductIds();
            for (Long productId : productIds) {
                userCartMap.remove(productId);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("CartServiceImpl.clearCartItemByUserID occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

}
