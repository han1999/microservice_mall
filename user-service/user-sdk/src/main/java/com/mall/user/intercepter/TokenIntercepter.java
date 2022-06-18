package com.mall.user.intercepter;

import com.alibaba.fastjson.JSON;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IUserLoginService;
import com.mall.user.annotation.Anonymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//import com.mall.user.dto.CheckAuthResponse;

/**
 * 用来实现token拦截认证
 * <p>
 * 其实就是用来判断当前这个操作是否需要登录
 */
public class TokenIntercepter extends HandlerInterceptorAdapter {

    @Reference(timeout = 3000,check = false, retries = 0)
    IUserLoginService userLoginService;

    public static String ACCESS_TOKEN = "access_token";

    public static String USER_INFO_KEY = "userInfo";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. handler 对象，指的是处理该请求的Controller类中对应方法
        // 2. 如果该请求是一个动态资源的请求，该action对应的对象的类型，就是HandlerMethod
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }


        // 如果代码运行到这里，意味者本次请求，一定是一个由某个Controller中的action处理的动态请求
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (isAnonymous(handlerMethod)) {
            return true;
        }

        // 代码运行到这里，就意味着，该请求是需要先登录在处理的请求
        String token = CookieUtil.getCookieValue(request, ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            ResponseData responseData = new ResponseUtil().setErrorMsg("token已失效");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(JSON.toJSON(responseData).toString());
            return false;
        }

        //从token中获取用户信息
        CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
        checkAuthRequest.setToken(token);//把cookie中token封装到checkAuthRequest中
        //CheckAuthResponse就在这里用过一次， 起一个中转作用, 主要为了查看，userInfo是否传入成功
        CheckAuthResponse checkAuthResponse= userLoginService.freeAndValidToken(checkAuthRequest);
        if(checkAuthResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            //request域中加了一些信息
            request.setAttribute(USER_INFO_KEY,checkAuthResponse.getUserInfo()); //保存token解析后的信息后续要用
            return super.preHandle(request, response, handler);//就是true
        }
        ResponseData responseData=new ResponseUtil().setErrorMsg(checkAuthResponse.getMsg());
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JSON.toJSON(responseData).toString());
        return false;
    }

    private boolean isAnonymous(HandlerMethod handlerMethod) {

        // 包含Controller处理方法的那个Controller对象
        Object bean = handlerMethod.getBean();

        // 获取Controller类对应的Class对象
        Class clazz = bean.getClass();
        // 如果说Controller类上 有Anoymous
        if (clazz.getAnnotation(Anonymous.class) != null) {
            return true;
        }

        // 获取action方法对应的Method对象
        Method method = handlerMethod.getMethod();
        // 如果在方法上获取到了Anoymous注解，返回true，否则返回false
        return method.getAnnotation(Anonymous.class) != null;
    }
}
