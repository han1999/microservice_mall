package com.mall.user;

import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;

/**
 * @description:
 * @author: hanxiao
 * @date: 2022/6/16
 **/


public interface IRegisterService {

    UserRegisterResponse register(UserRegisterRequest registerRequest);
}
