package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IloginOutService;
import com.mall.user.intercepter.TokenIntercepter;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/17
 **/
@RestController
@RequestMapping("/user")
public class loginOutController {

    @Reference(timeout = 3000, retries = 0, check = false)
    IloginOutService loginOutService;

    /**
     * loginOut-->logout 更合理一点
     * 感觉退出，没有什么业务
     * 不对，应该有一个业务，就是让token失效
     * 这个业务太简单了，都涉及不到dao层面，没有必要用service
     * @return
     */
    @GetMapping("/loginOut")
    public ResponseData loginOut(HttpServletRequest request, HttpServletResponse response) {
        //maxAge用来删除cookie
        Cookie cookie = CookieUtil.genCookie(TokenIntercepter.ACCESS_TOKEN, "", "/", 0);
        response.addCookie(cookie);
        return new ResponseUtil<>().setData(null);
    }
}
