package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.IRegisterService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.KaptchaCodeRequest;
import com.mall.user.dto.KaptchaCodeResponse;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/14
 **/

@RestController
@RequestMapping("/user")
public class RegisterController {
    @Reference
    private IKaptchaService kaptchaservice;

    @Reference
    private IRegisterService registerService;


    @PostMapping("/register")
    @Anoymous
    public ResponseData register(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String userName = map.get("userName");
        String userPwd = map.get("userPwd");
        String email = map.get("email");
        String captcha = map.get("captcha");

        //验证码
        String kaptcha_uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest(kaptcha_uuid, captcha);
//        kaptchaCodeRequest.setUuid(kaptcha_uuid);
//        kaptchaCodeRequest.setCode(captcha);
        KaptchaCodeResponse kaptchaCodeResponse = kaptchaservice.validateKaptchaCode(kaptchaCodeRequest);
        if (!kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setErrorMsg(kaptchaCodeResponse.getMsg());
        }

//        SysRetCodeConstants 包含 所有关于 user_service的返回信息
        //注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest(userName, userPwd, email);
        UserRegisterResponse userRegisterResponse = registerService.register(userRegisterRequest);
        if (userRegisterResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(userRegisterResponse.getMsg());

    }
}
