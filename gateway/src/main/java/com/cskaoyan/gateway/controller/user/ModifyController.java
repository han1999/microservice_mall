package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.IMemberService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.HeadImageRequest;
import com.mall.user.dto.HeadImageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/7/19
 **/

@RestController
@Slf4j
@RequestMapping("/user")
public class ModifyController {

    @Reference(timeout = 3000, retries = 0, check = false)
    IMemberService memberService;

    /**
     * 好家伙，前端写错了，前段时间imgaeUpload， 找半天才发现这个bug
     * @param request
     * @return
     */
    @PostMapping("/imgaeUpload")
    public ResponseData imageUpload(@RequestBody HeadImageRequest request) {
        HeadImageResponse response = memberService.updateHeadImage(request);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            return new ResponseUtil<>().setData(response.getMsg());
        }
        return new ResponseUtil<>().setErrorMsg(response.getMsg());
    }
}
