package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.user.dal.entitys.Member;
import org.junit.Test;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/

public class JsonTest {
    @Test
    public void testOneParamJson() {
        int uid = 100;
        String jsonString = JSON.toJSONString(uid);
        System.out.println("jsonString = " + jsonString);

        String myString = "{\"uid\":" + uid + "}";
        System.out.println("myString = " + myString);
        JSONObject jsonObject = JSON.parseObject(myString);
        Integer uid1 = jsonObject.getObject("uid1", Integer.class);
        System.out.println("uid1 = " + uid1);
    }

    @Test
    public void test2() {
//        LoginUser loginUser = new LoginUser();
//        loginUser.setCaptcha("1234");
//        loginUser.setUserName("hanxiao");
//        String jsonString = JSON.toJSONString(loginUser);
//        System.out.println("jsonString = " + jsonString);
        /**
         * 测试结果，JSON只会封装，非null的属性
         * 对于非字符串属性，不会加上""
         */
        Member member = new Member();
        member.setId(100L);
        member.setUsername("hanxiao");
        String jsonString = JSON.toJSONString(member);
        System.out.println("jsonString = " + jsonString);
//        jsonString = {"id":100,"username":"hanxiao"}

        /**
         * myString就是把jsonString里面的id属性加上""
         * 也能成功解析
         */
        String myString = "{\"id\":\"100\",\"username\":\"hanxiao\"}";
        System.out.println("myString = " + myString);
        JSONObject jsonObject = JSON.parseObject(myString);
        Long id = jsonObject.getObject("id", Long.class);
        System.out.println("id = " + id);

        Member member1 = JSON.parseObject(myString, Member.class);
        System.out.println("member1 = " + member1);

        Member member2 = JSON.parseObject(jsonString, Member.class);
        System.out.println("member2 = " + member2);

    }
}
