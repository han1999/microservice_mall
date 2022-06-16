package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *  cskaoyan
 */
@Data
public class UserRegisterRequest extends AbstractRequest {

    private String userName;
    private String userPwd;
    private String email;

    @Override
    public void requestCheck() {
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(userPwd)){
            throw new ValidateException(SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }

    public UserRegisterRequest(String userName, String userPwd, String email) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.email = email;
    }
}
