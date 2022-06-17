package com.mall.user;

import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

public interface IUserLoginService {

    /**
     * 获取登录的token
     * @param request
     * @return
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 验证token， 成功则向response中写入userInfo
     * @param checkAuthRequest
     * @return
     */
    CheckAuthResponse validToken(CheckAuthRequest checkAuthRequest);
}
