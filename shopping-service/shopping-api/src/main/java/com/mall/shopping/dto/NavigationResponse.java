package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@Data
public class NavigationResponse extends AbstractResponse {
    private List<PanelContentDto> panelContentDtoList;
}
