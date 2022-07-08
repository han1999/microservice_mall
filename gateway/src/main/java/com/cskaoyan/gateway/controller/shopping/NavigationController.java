package com.cskaoyan.gateway.controller.shopping;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.shopping.INavigationService;
import com.mall.shopping.dto.NavigationResponse;
import com.mall.user.annotation.Anonymous;
import com.mall.user.constants.SysRetCodeConstants;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@RestController
@RequestMapping("/shopping")
public class NavigationController {

    @Reference(timeout = 3000, retries = 0, check = false)
    INavigationService navigationService;

    @GetMapping("/navigation")
    @Anonymous
    public ResponseData navigation() {
        NavigationResponse response = navigationService.navigation();
        if (SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getPanelContentDtoList());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
