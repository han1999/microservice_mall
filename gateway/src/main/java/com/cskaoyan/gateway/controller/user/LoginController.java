package com.cskaoyan.gateway.controller.user;

import com.alibaba.fastjson.JSON;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.IUserLoginService;
import com.mall.user.annotation.Anonymous;
import com.mall.user.bo.LoginUser;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.CheckAuthUserInfo;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;
import com.mall.user.intercepter.TokenIntercepter;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @Anonymous
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

    /**
     * @param request
     * @return
     */
    @GetMapping("/login")
    public ResponseData validateLoginToken(HttpServletRequest request) {
//        CheckAuthUserInfo userInfo = (CheckAuthUserInfo) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        String userInfoString = (String) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        CheckAuthUserInfo userInfo = JSON.parseObject(userInfoString, CheckAuthUserInfo.class);
        return new ResponseUtil<>().setData(userInfo);
        // TODO: 2022/6/17  以上代码，可能要重构，改成根据uid查询数据库
    }
}
