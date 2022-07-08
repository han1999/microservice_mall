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
     * 验证token， 将token解析为message，验证， 成功则向CheckAuthResponse中写入userInfo(也就是message，Json格式)
     * CheckAuthResponse得到userInfo后，如果CheckAuthResponse的状态码正确，马上把userInfo传给request
     * @param checkAuthRequest
     * @return
     */
    CheckAuthResponse freeAndValidToken(CheckAuthRequest checkAuthRequest);

    /**
     * 验证用户登录,根据uid查询是否存在该用户，并返回最新用户信息(uid, file)
     * @param request
     * @return
     */
    CheckLoginResponse checkLogin(CheckLoginRequest request);
}
