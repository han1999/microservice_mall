package com.mall.shopping.converter;

import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dto.ProductDetailDto;
import com.mall.shopping.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * cskaoyan
 */
@Mapper(componentModel = "spring")
public interface ProductConverter {

    @Mappings({
            @Mapping(source = "id", target = "productId"),
            @Mapping(source = "title", target = "productName"),
            @Mapping(source = "price", target = "salePrice"),
            @Mapping(source = "sellPoint", target = "subTitle"),
            @Mapping(source = "imageBig", target = "picUrl")
    })
    ProductDto item2Dto(Item item);

    /**
     *  Item.images是String[]类型， ProductDetailDto.productImageSmall是List<String>
     *  String[]-->List<String>是可以成功的
     * @param item
     * @return
     */
    @Mappings({
            @Mapping(source = "id", target = "productId"),
            @Mapping(source = "price", target = "salePrice"),
            @Mapping(source = "title", target = "productName"),
            @Mapping(source = "sellPoint", target = "subTitle"),
            @Mapping(source = "imageBig", target = "productImageBig"),
            @Mapping(source = "images", target = "productImageSmall")
    })
    ProductDetailDto item2ProductDetailDto(Item item);

    List<ProductDto> items2Dto(List<Item> items);


}
