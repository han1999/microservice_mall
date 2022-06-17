package com.cskaoyan.gateway.controller.user;

import com.mall.user.IloginOutService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return
     */
//    @GetMapping("/loginOut")
//    public ResponseData loginOut() {
//
//    }
}
