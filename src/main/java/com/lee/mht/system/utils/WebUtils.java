package com.lee.mht.system.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author FucXing
 * @date 2021/12/24 23:58
 **/
public class WebUtils {
    /*
     * 得到requeset
     * */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }
    /*
     * 得到session
     * */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

}
