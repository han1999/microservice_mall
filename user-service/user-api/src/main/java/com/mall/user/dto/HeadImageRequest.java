package com.mall.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 */
@Data
public class HeadImageRequest extends AbstractRequest {

    private Long userId;

    @JsonProperty("imgData")
    private String imageData;


    @Override
    public void requestCheck() {
        if (userId == null || userId < 0 || StringUtils.isEmpty(imageData)) {
            throw new ValidateException(SysRetCodeConstants.REQUEST_DATA_NOT_EXIST.getCode(), SysRetCodeConstants.REQUEST_DATA_NOT_EXIST.getMessage());
        }
    }
}
