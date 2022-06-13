package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 */
@Data
public class HeadImageRequest extends AbstractRequest {
    private Long userId;
    private String imageData;

    @Override
    public void requestCheck() {

    }
}
