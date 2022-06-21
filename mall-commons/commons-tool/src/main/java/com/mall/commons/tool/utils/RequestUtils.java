package com.mall.commons.tool.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/21
 **/

public class RequestUtils {
    public static <T> T getStringAttributeJsonValue(HttpServletRequest request, String attributeKey, String jsonKey,  Class<T> tClass) {
        String attribute = (String) request.getAttribute(attributeKey);
        JSONObject jsonObject = JSON.parseObject(attribute);
        T value = jsonObject.getObject(jsonKey, tClass);
        return value;
    }
}
