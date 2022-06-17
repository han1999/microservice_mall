package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.IHomeService;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.user.annotation.Anonymous;
import com.mall.user.constants.SysRetCodeConstants;
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
@RequestMapping("/shopping")
public class HomepageController {

    @Reference(timeout = 3000, retries = 0, check = false)
    IHomeService homeService;

    @GetMapping("/homepage")
    @Anonymous
    public ResponseData homepage() {
        HomePageResponse response = homeService.homepage();
        if (SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getPanelContentItemDtos());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
