package com.cskaoyan.gateway.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.IUserLoginService;
import com.mall.user.annotation.Anonymous;
import com.mall.user.bo.LoginUser;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.*;
import com.mall.user.intercepter.TokenIntercepter;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Reference(timeout = 3000, retries = 0, check = false)
    IKaptchaService kaptchaService;

    /**
     * 这里登录还需要验证码就很不合理！！！ 但是既然都这么设计了，也就照业务来写了
     */
    @PostMapping("/login")
    @Anonymous
    public ResponseData login(@RequestBody LoginUser loginUser, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        /**
         * 验证码校验
         */
        String kaptcha_uuid = CookieUtil.getCookieValue(httpServletRequest, "kaptcha_uuid");
        KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
        kaptchaCodeRequest.setUuid(kaptcha_uuid);
        kaptchaCodeRequest.setCode(loginUser.getCaptcha());
        KaptchaCodeResponse kaptchaCodeResponse = kaptchaService.validateKaptchaCode(kaptchaCodeRequest);
        if (!SysRetCodeConstants.SUCCESS.getCode().equals(kaptchaCodeResponse.getCode())) {
            return new ResponseUtil<>().setErrorMsg(kaptchaCodeResponse.getMsg());
        }

        /**
         * 登录
         */
        UserLoginRequest request = new UserLoginRequest();
        request.setUserName(loginUser.getUserName());
        request.setPassword(loginUser.getUserPwd());
        UserLoginResponse response = userLoginService.login(request);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            /**
             * 成功登录， 给一个access_token到cookie里面， 我猜是这个逻辑
             * 确实是这个逻辑！
             */
            Cookie cookie = CookieUtil.genCookie(TokenIntercepter.ACCESS_TOKEN, response.getToken(), "/", 3600 * 24);
            cookie.setHttpOnly(true);
            httpServletResponse.addCookie(cookie);
            return new ResponseUtil<>().setData(response);
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }

    /**
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/login")
    public ResponseData checkLogin(HttpServletRequest httpServletRequest) {
//        CheckAuthUserInfo userInfo = (CheckAuthUserInfo) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
/*        String userInfoString = (String) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        CheckAuthUserInfo userInfo = JSON.parseObject(userInfoString, CheckAuthUserInfo.class);
        return new ResponseUtil<>().setData(userInfo);*/
        // TODO: 2022/6/17  以上代码，可能要重构，改成根据uid查询数据库 Complete
        String userInfo = (String) httpServletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        JSONObject jsonObject = JSON.parseObject(userInfo);
        Long uid = jsonObject.getObject("uid", Long.class);

        CheckLoginRequest request = new CheckLoginRequest();
        request.setUid(uid);
        CheckLoginResponse response = userLoginService.checkLogin(request);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getCheckLoginMemberDto());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
