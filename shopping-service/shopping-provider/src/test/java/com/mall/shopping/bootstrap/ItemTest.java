package com.mall.shopping.bootstrap;

import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.persistence.ItemMapper;
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

    @Test
    public void testItemOrm() {
        Item item = itemMapper.selectByPrimaryKey(100023501);
        System.out.println("item = " + item);
    }

}
