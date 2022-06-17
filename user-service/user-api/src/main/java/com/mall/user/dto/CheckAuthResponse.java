package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

@Data
public class CheckAuthResponse extends AbstractResponse {
    //    private CheckAuthUserInfo userInfo;
    private String userInfo;
}
