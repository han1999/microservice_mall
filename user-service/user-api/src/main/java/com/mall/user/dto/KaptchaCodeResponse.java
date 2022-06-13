package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *
 */
@Data
public class KaptchaCodeResponse extends AbstractResponse {

    private String imageCode;

    private String uuid;


}
