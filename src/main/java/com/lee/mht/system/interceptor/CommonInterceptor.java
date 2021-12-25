package com.lee.mht.system.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FucXing
 * @date 2021/12/23 20:19
 **/
@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    //允许跨域
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (request.getMethod().equals("OPTIONS")) {
            response.addHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        }
        return true;
    }

}
