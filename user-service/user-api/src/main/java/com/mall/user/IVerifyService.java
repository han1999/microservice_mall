package com.mall.user;

import com.mall.user.dto.UserVerifyRequest;
import com.mall.user.dto.UserVerifyResponse;

/**
 * @description:
 * @author: hanxiao
 * @date: 2022/6/17
 **/
public interface IVerifyService {

    UserVerifyResponse verify(UserVerifyRequest request);
}
