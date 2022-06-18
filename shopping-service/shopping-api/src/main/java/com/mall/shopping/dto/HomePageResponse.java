package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  cskaoyan
 */
@Data
public class HomePageResponse extends AbstractResponse {

//    private Set<PanelDto> panelContentItemDtos;
    /**
     * List明显比set合理 这样就可以按照顺序显示了
     */
    private List<PanelDto> panelContentItemDtos;
}
