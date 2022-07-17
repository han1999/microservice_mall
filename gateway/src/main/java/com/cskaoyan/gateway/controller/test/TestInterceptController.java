package com.cskaoyan.gateway.controller.test;

import com.mall.user.annotation.Anonymous;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestInterceptController {

    @GetMapping("/intercept")
    @Anonymous
    public String testIntercept(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        log.info("Content-Type: {}", contentType);
//        System.out.println("contentType = " + contentType);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("username".equals(cookie.getName())) {
                String value = cookie.getValue();
                log.info("value: {}", value);
//                System.out.println("value = " + value);
                break;
            }
        }
        return "hello, token interceptor";
    }

}
