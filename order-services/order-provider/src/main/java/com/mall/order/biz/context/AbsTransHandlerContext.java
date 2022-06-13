package com.mall.order.biz.context;

import com.mall.order.biz.convert.TransConvert;
import lombok.Data;

/**
 *  cskaoyan
 * 交易相关的抽象
 */
@Data
public abstract class AbsTransHandlerContext implements TransHandlerContext {

    private String orderId;

    private TransConvert convert = null;


}
