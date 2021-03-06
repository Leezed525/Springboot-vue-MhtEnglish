package com.lee.mht.system.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2021/12/23 15:31
 **/

@Configuration
public class ShiroConfig {



    //自定义登录校验器
    @Bean
    public LeeMatcher leeMatcher(){
        return new LeeMatcher();
    }


    @Value("${LeeMht.redisHost}")
    private String redisHost;

    @Value("${LeeMht.redisPassword}")
    private String redisPassword;

    /**
     * 创建自定义的验证规则
     */
    @Bean
    public Realm myRealm() {
        LeeRealm leeRealm = new LeeRealm();
        //设置校验器
        leeRealm.setCredentialsMatcher(leeMatcher());

        //设置缓存

        // 开启缓存
        leeRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        leeRealm.setAuthenticationCachingEnabled(true);
        leeRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        leeRealm.setAuthorizationCachingEnabled(true);
        leeRealm.setAuthorizationCacheName("authorizationCache");
        leeRealm.setCacheManager(cacheManager());
        return leeRealm;
    }

    /**
     * 创建安全管理
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm());

        manager.setCacheManager(cacheManager());


        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    //配置权限过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //SecurityManager 安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();//拦截器, 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/druid/**","anon");
        filterChainDefinitionMap.put("/business/login","anon");
        filterChainDefinitionMap.put("/admin/system/login","anon");
        //放行websocket请求
        filterChainDefinitionMap.put("/adminNoticeSocket/**", "anon");
        filterChainDefinitionMap.put("/userNoticeSocket/**", "anon");
        //下面这一行是为了防止请求druid时报错，虽然无伤大雅，但是看着不爽（druid能打开，但是会在控制台输出错误，因为这个请求进了jwtFilter）
        filterChainDefinitionMap.put("/favicon.ico","anon");

        //filterChainDefinitionMap.put("/admin/user/getAllAdminUser", "perms[adminUser:query]");
        //filterChainDefinitionMap.put("/**", "authc");    //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
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



    public RedisManager redisManager(){
        RedisManager redisManager=new RedisManager();

        redisManager.setHost(redisHost);
        redisManager.setPassword(redisPassword);
        redisManager.setTimeout(2000);
        return redisManager;
    }


    /**
     * @author  zhuyang
     * @description  缓存管理器
     * @date 2021-03-06 17:31
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
}
