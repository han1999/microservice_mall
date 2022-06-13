package com.mall.order.biz.factory;/**
 * Created  on 2019/8/2.
 */

import com.mall.order.biz.TransOutboundInvoker;

/**
 *
 */
public interface TransPipelineFactory<T> {

    TransOutboundInvoker build(T obj);
}
