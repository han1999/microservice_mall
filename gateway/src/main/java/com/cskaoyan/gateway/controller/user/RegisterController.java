package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.IRegisterService;
import com.mall.user.annotation.Anonymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.KaptchaCodeRequest;
import com.mall.user.dto.KaptchaCodeResponse;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RegisterController {
    @Reference(timeout = 3000, retries = 0, check = false)
    private IKaptchaService kaptchaservice;

    @Reference(timeout = 3000, retries = 0, check = false)
    private IRegisterService registerService;


    @PostMapping("/register")
    @Anonymous
    public ResponseData register(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String userName = map.get("userName");
        String userPwd = map.get("userPwd");
        String email = map.get("email");
        String captcha = map.get("captcha");
        log.info("username:{}  password:{}  email:{}",userName, userPwd, email);
        //验证码
        KaptchaCodeResponse kaptchaCodeResponse = getKaptchaCodeResponse(request, captcha);
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

    /**
     * 验证码检查
     * @param request
     * @param captcha
     * @return
     */
    private KaptchaCodeResponse getKaptchaCodeResponse(HttpServletRequest request, String captcha) {
        String kaptcha_uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
        kaptchaCodeRequest.setUuid(kaptcha_uuid);
        kaptchaCodeRequest.setCode(captcha);
        return kaptchaservice.validateKaptchaCode(kaptchaCodeRequest);
    }
}
