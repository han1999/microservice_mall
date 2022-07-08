package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@Data
public class CheckLoginRequest extends AbstractRequest {
    Long uid;

    @Override
    public void requestCheck() {
        if (uid == null || uid < 0) {
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage()
            );
        }
    }
}
