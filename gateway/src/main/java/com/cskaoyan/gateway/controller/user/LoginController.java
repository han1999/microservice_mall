package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.IUserLoginService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.bo.LoginUser;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

@RestController
@RequestMapping("/user")
public class LoginController {
    //这个@Reference导入错了，导致一个bug难以发现
    @Reference(timeout = 3000, retries = 0, check = false)
    IUserLoginService userLoginService;

    @PostMapping("/login")
    @Anoymous
    public ResponseData login(@RequestBody LoginUser loginUser) {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserName(loginUser.getUserName());
        request.setPassword(loginUser.getUserPwd());
        UserLoginResponse response = userLoginService.login(request);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
