package com.mall.pay.utils;

import com.mall.commons.result.AbstractResponse;
import com.mall.pay.constants.PayReturnCodeEnum;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

public class ResponseUtils {
    //static方法，先声明一个泛型 不需要类上声明

    /**
     * 这个写法真是不错
     * 如果在具体的response下面写这个方法，那么要写的太多
     * 如果在AbstractResponse下面写，则shopping等服务继承的话，就没用，浪费资源，耦合了
     * 如果在mall-commons中写的话，可能会有循环依赖
     * （其他依赖于mall-commons， mall-commons依赖于其他，这样可能会混乱)
     * @param response
     * @param constant
     * @param <T>
     * @return
     */
    public static <T extends AbstractResponse> T setCodeAndMsg(T response, PayReturnCodeEnum constant) {
        response.setCode(constant.getCode());
        response.setMsg(constant.getMsg());
        return response;
    }
}
