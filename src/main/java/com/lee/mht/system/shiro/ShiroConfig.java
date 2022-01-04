package com.lee.mht.system.shiro;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2021/12/23 15:31
 **/

@Configuration
public class ShiroConfig {

    @Bean
    public LeeMatcher leeMatcher(){
        return new LeeMatcher();
    }

    /**
     * 创建自定义的验证规则
     */
    @Bean
    public Realm myRealm() {
        LeeRealm leeRealm = new LeeRealm();
        //设置校验器
        leeRealm.setCredentialsMatcher(leeMatcher());
        //设置缓存(还没设)
        return leeRealm;
    }

    /**
     * 创建安全管理
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm());

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    //配置权限过滤器，以下内容是抄的，先把框架搭出来，到时候再改
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //SecurityManager 安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();//拦截器, 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/druid/**","anon");
        filterChainDefinitionMap.put("/admin/system/login","anon");
        //filterChainDefinitionMap.put("/admin/user/getAllAdminUser", "perms[adminUser:query]");
        filterChainDefinitionMap.put("/**", "authc");    //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        //filterChainDefinitionMap.put("/**", "user");   //user表示配置记住我或认证通过可以访问的地址

        // 添加自己的过滤器并且取名为jwt
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // 过滤链定义，从上向下顺序执行，一般将放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    //不能注册成Bean不然springboot会先接手这个bean，然后原先的公共资源就不能访问了
    //@Bean
    //public JwtFilter jwtFilter() {
    //    return new JwtFilter();
    //}

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv;
        System.out.println(ex instanceof UnauthenticatedException);
        //进行异常判断。如果捕获异常请求跳转。
        if (ex instanceof UnauthorizedException) {
            mv = new ModelAndView("/error/error");
            ex.printStackTrace();
            mv.addObject("msg", "你的级别还不够高,加油吧！少年。");
            return mv;
        }
        else if(ex instanceof UnauthenticatedException){
            mv = new ModelAndView("/error/error");
            ex.printStackTrace();
            mv.addObject("msg", "没有此权限！");
            return mv;
        }
        else {
            mv = new ModelAndView("/error/error");
            ex.printStackTrace();
            mv.addObject("msg", "我勒个去，页面被外星人挟持了!");
            return mv;

        }

    }


    /**
     * 下面两个Bean用于开启shiro aop注解支持.
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
