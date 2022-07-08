package com.mall.shopping.dto;/**
 * Created  on 2019/7/29.
 */

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * 其实根本不需要搞个List， 因为就一个元素
 * 但是前后端接口就按照list来设计的，就遵循吧
 */
@Data
public class RecommendResponse extends AbstractResponse{

    private List<PanelDto> panelDtoList;

}
