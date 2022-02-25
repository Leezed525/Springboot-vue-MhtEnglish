package com.lee.mht.system.Interceptor;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author FucXing
 * @date 2022/02/22 17:29
 **/
@Component
@Slf4j
public class HitCountInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //log.info(String.valueOf(request.getRequestURL()));
        //int userId = JwtUtils.getId(request);
        //if (redisService.isLimitExceeded(userId)) {
        //    CommonResponse(Constant.OK,Constant.LIMIT_EXCEEDED_CODE,Constant.LIMIT_EXCEEDED_MSG,response);
        //    return false;
        //}
        return true;
    }
    /**
     * 自定义错误响应
     * filter的错误比自定义的错误先判，所以要在这里单独写一个判断
     */
    private void CommonResponse(int statusCode,int code, String msg, ServletResponse resp) {
        // 自定义异常的类，用户返回给客户端相应的JSON格式的信息
        try {
            HttpServletResponse response = (HttpServletResponse) resp;
            ResultObj result = new ResultObj(code, msg, null);
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(statusCode);
            String userJson = JacksonUtils.toJson(result);
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            log.error("error={}", e.getMessage());
        }
    }
}
