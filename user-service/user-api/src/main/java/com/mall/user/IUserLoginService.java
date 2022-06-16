package com.mall.user;

import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

public interface IUserLoginService {
    UserLoginResponse login(UserLoginRequest request);
}
