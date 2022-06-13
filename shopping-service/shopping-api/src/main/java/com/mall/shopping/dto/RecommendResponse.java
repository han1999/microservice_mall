package com.mall.shopping.dto;/**
 * Created  on 2019/7/29.
 */

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.Set;

/**
 *
 */
@Data
public class RecommendResponse extends AbstractResponse{

    private Set<PanelDto> panelContentItemDtos;

}
