package com.mall.shopping.bootstrap;


import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    ItemMapper itemMapper;

//    @Reference
//    ITestProductDetailService productDetailService;


    @Test
    public void testMapper() {
        Item item = itemMapper.selectByPrimaryKey(100023501);
        System.out.println(item);
    }

    @Test
    public void testService() {
    }

}
