package com.mall.order.biz;

import com.mall.order.biz.context.TransHandlerContext;

/**
 *  cskaoyan
 */
public interface TransOutboundInvoker {

    /**
     * 启动流程.<br/>
     *
     */
    void start();

    /**
     * 终止流程.<br/>
     *
     */
    void shutdown();

    /**
     * 用于获取返回值<br/>
     *
     * @return
     */
    <T extends TransHandlerContext> T getContext();
}
