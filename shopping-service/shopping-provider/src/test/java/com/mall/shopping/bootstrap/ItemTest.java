package com.mall.shopping.bootstrap;

import com.mall.shopping.converter.ProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dto.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTest {
    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ProductConverter productConverter;

    @Test
    public void testItemOrm() {
        Item item = itemMapper.selectByPrimaryKey(100023501);
        System.out.println("item = " + item);
 /*       public ProductDto item2Dto(Item item) {
            if ( item == null ) {
                return null;
            }

            ProductDto productDto = new ProductDto();

            productDto.setPicUrl( item.getImageBig() );
            productDto.setProductId( item.getId() );
            productDto.setSubTitle( item.getSellPoint() );
            productDto.setSalePrice( item.getPrice() );
            productDto.setProductName( item.getTitle() );

            return productDto;
        }*/
        /*
        以上是ProductConverter.item2Dto实现过程，其中productDto.setPicUrl(item.getImageBig());
        已经调用了item.getImageBig()，所以，item中虽然imageBig==null, 但是dto中picUrl是有值的
        并且在DO中不需要有setImageBig(),直接在getImageBig中写逻辑就行了
         */
        ProductDto productDto = productConverter.item2Dto(item);
        System.out.println("productDto = " + productDto);

    }

}
