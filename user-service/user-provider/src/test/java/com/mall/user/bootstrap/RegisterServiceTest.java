package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.user.IRegisterService;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

public class RegisterServiceTest extends UserProviderApplicationTests{
    @Autowired
    IRegisterService registerService;

    @Test
    public void testRegister() {
//        UserRegisterRequest request = new UserRegisterRequest("hanxiao", "123456", "1043599451@qq.com");
//        UserRegisterRequest request = new UserRegisterRequest("lisi", "789456", "2468244963@qq.com");
        UserRegisterRequest request = new UserRegisterRequest("hanxiao", "123456", "1043599451@qq.com");
        UserRegisterResponse response = registerService.register(request);
        String responseJson = JSON.toJSON(response).toString();
        System.out.println("responseJson = " + responseJson);
    }
}
