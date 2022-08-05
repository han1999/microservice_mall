package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.IVerifyService;
import com.mall.user.annotation.Anonymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.UserVerifyRequest;
import com.mall.user.dto.UserVerifyResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/17
 **/
@RestController
@RequestMapping("/user")
public class VerifyController {

    @Reference(timeout = 3000, retries = 0, check = false)
    IVerifyService verifyService;

    @GetMapping("/verify")
    @Anonymous
    public ResponseData verify(String uid, String username) {
        UserVerifyRequest request = new UserVerifyRequest();
        request.setUserName(username);
        request.setUuid(uid);
        UserVerifyResponse response = verifyService.verify(request);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData("验证成功，快去登录吧： http://118.178.135.74/#/login");
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
