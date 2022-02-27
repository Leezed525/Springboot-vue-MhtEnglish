package com.lee.mht.system.config;

import com.lee.mht.system.Interceptor.HitCountInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FucXing
 * @date 2022/02/22 17:34
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public HitCountInterceptor getHitCountInterceptor() {
        return new HitCountInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册 MyHandler 拦截器
        InterceptorRegistration hitCountInterceptor = registry.addInterceptor(getHitCountInterceptor());
        //  /**表示拦截所有请求
        hitCountInterceptor.addPathPatterns("/**");
        // 放行下面的请求，一般是静态资源
        hitCountInterceptor.excludePathPatterns("/druid/**",
                                            "/business/login",
                                            "/admin/system/login",
                                            "/adminNoticeSocket/**",
                                            "/userNoticeSocket/**");
    }
}
