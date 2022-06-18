package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@Data
public class CheckLoginResponse extends AbstractResponse {
    private CheckLoginMemberDto checkLoginMemberDto;
}
