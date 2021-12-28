package com.lee.mht.system.shiro;

import com.lee.mht.system.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FucXing
 * @date 2021/12/27 21:57
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    //@Autowired
    //private RedisUtil redisUtil;


    private AntPathMatcher antPathMatcher =new AntPathMatcher();
    /**
     * 执行登录认证(判断请求头是否带上token)
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //是否是尝试登录，如果尝试登陆直接返回true
        if (isLoginAttempt(request, response)) {
            return true;
        }
        //如果不是访问登录api，正常的情况下HEADER中应该有token
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(Constant.HEADER_TOKEN_KEY);
        if(StringUtils.hasLength(token)){
            //如果存在,则进入executeLogin方法执行登入,检查token 是否正确
            //从redis中去查看这个token是否已经验证通过了
            //如果通过了直接返回true
            //否则去验证进入login方法
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                throw new AuthenticationException("Token非法，请登出后重新登陆，请勿篡改Token");
            }
        }
        else{
            //无token非法访问请求
            throw new AuthenticationException("非法访问");
        }
    }

    /**
     * 判断用户是否是登入,检测headers里是否包含token字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        if(antPathMatcher.match("/admin/system/login",req.getRequestURI())){
            return true;
        }
        return false;
        //String token = req.getHeader(Constant.HEADER_TOKEN_KEY);
    }
    /**
     * 重写AuthenticatingFilter的executeLogin方法执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
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
}
