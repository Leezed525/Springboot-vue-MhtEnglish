package com.lee.mht.system.shiro;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.exception.SystemException;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author FucXing
 * @date 2021/12/27 21:57
 **/
@Slf4j
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {
    //@Autowired
    //private RedisUtil redisUtil;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 执行登录认证(判断请求头是否带上token)
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("请求的地址是{}",((HttpServletRequest)request).getRequestURL());
        //是否是尝试登录，如果尝试登陆直接返回true
        if (isLoginAttempt(request, response)) {
            return true;
        }
        try {
            executeLogin(request, response);
            return true;
        }catch (SystemException e) {
            CommonResponse(e.getCode(),e.getCode(),e.getDefaultMessage(),response);
            return false;
        } catch (AuthenticationException e){//如果抓到这个错误
            if(e.getCause() instanceof SystemException){
                SystemException exception= (SystemException) e.getCause();
                CommonResponse(exception.getCode(),exception.getCode(),exception.getDefaultMessage(),response);
            }else {
                CommonResponse(Constant.SERVER_ERROR_CODE,Constant.SERVER_ERROR_CODE,Constant.SHIRO_AUTHENTICATION_ERROR,response);
            }
            return false;
        }
    }



    /**
     * 判断用户是否是登入,检测headers里是否包含token字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        return antPathMatcher.match("/admin/system/login", req.getRequestURI());
        //String token = req.getHeader(Constant.HEADER_TOKEN_KEY);
    }

    /**
     * 重写AuthenticatingFilter的executeLogin方法执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException{
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Constant.HEADER_TOKEN_KEY);//Access-Token获取token
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入,如果错误他会抛出异常并被捕获, 反之则代表登入成功,返回true
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    private boolean isLimitExceeded(ServletRequest request) {
        int userId = JwtUtils.getId((HttpServletRequest) request);
        return redisService.isLimitExceeded(userId);
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
