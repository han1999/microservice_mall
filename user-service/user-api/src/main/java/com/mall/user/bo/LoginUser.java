package com.mall.user.bo;

import lombok.Data;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/
@Data
public class LoginUser {
    private String userName;
    private String userPwd;
    private String captcha;
}
