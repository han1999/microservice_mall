package com.mall.order.converter;

import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 *  cskaoyan
 */
@Mapper(componentModel = "spring")
public interface OrderConverter {

    @Mappings({})
    OrderDetailResponse order2res(Order order);

    @Mappings({})
    OrderDetailInfo order2detail(Order order);

//    List<OrderDetailInfo> orders2Details(List<Order> orderList);

    @Mappings({
            @Mapping(source = "status", target = "orderStatus")
    })
    OrderItemDto item2dto(OrderItem item);

    @Mappings({})
    OrderShippingDto shipping2dto(OrderShipping shipping);


    List<OrderItemDto> items2dtos(List<OrderItem> items);

    @Mappings({})
    OrderItemResponse item2res(OrderItem orderItem);

    @Mappings({})
    OrderDto order2dto(Order order);

    @Mappings({})
    List<OrderDto> orders2Dtos(List<Order> orderList);
}
