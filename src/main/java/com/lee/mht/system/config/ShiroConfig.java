package com.lee.mht.system.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;

/**
 * @author FucXing
 * @date 2021/12/23 15:31
 **/

@Configuration
public class ShiroConfig {
    /**
     * 创建自定义的验证规则
     */
    @Bean
    public Realm myRealm() {
        return new LeeRealm();
    }

    /**
     * 创建安全管理
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm());
        return manager;
    }

    //配置权限过滤器，以下内容是抄的，先把框架搭出来，到时候再改
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //SecurityManager 安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //登录，为后台接口名，非前台页面名, 未登录跳转到这里
        shiroFilterFactoryBean.setLoginUrl("/pub/login");
        //登录成功后跳转的地址，为后台接口名，非前台页面名
        shiroFilterFactoryBean.setSuccessUrl("/api/index");
        //无权限跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/unauthorized");
        // 过滤链定义，从上向下顺序执行,必须保证是有序的，所以用linked
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<String, String>();
        //公开的接口，都能访问
        //filterMap.put("/", "anon");
        //filterMap.put("/pub/**", "anon");
        ////api下的接口需要认证后才能访问
        //filterMap.put("/api/**", "autch");
        ////roles表示需要特定的角色才能访问
        //filterMap.put("/user/**", "roles[user]");
        //filterMap.put("/admin/**", "roles[admin]");
        //filterMap.put("/root/**", "roles[root]");
        //防止有忘记写的接口，剩下的都需要认证才能访问
        //filterMap.put("/**", "autch");
        //logout是shiro提供的过滤器
        //filterMap.put("/logout", "logout");


        //先放行所有接口，虽然现在就没写多少接口
        filterMap.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }
}
