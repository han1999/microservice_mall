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
    //captcha不属于用户的属性
    private String captcha;
}
