package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  cskaoyan
 */
@Data
public class AddressListResponse extends AbstractResponse {

    private List<AddressDto> addressDtos;
}
