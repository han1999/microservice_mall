package com.mall.order.biz.callback;

import com.mall.order.biz.context.TransHandlerContext;

/**
 *
 *
 * 交易回调
 */
public interface TransCallback {

    void onDone(TransHandlerContext context);
}
