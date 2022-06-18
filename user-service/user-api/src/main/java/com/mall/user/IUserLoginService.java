package com.mall.user;

import com.mall.user.dto.*;

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
     * 验证token， 成功则向response中写入userInfo(Json格式)
     * @param checkAuthRequest
     * @return
     */
    CheckAuthResponse validToken(CheckAuthRequest checkAuthRequest);

    CheckLoginResponse checkLogin(CheckLoginRequest request);
}
