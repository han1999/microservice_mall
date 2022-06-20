package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 *  cskaoyan
 */
@Data
public class AllProductRequest extends AbstractRequest {

    private Integer page;
    private Integer size;
    private String sort;
    private Long cid;
    private Integer priceGt;
    private Integer priceLte;

    @Override
    public void requestCheck() {
        if(page<=0){
            /**
             * 这里不抛异常，先让页面正常显示出来
             */
            setPage(1);
        }
    }
}
