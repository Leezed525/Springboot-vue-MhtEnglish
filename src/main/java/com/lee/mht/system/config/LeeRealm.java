package com.lee.mht.system.config;

import com.lee.mht.system.dao.AdminUserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author FucXing
 * @date 2021/12/23 12:53
 **/
@Slf4j
@Component
public class LeeRealm extends AuthorizingRealm {


    @Autowired(required = false)
    @Lazy
    private AdminUserDao adminUserDao;

    //用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //用户登入
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("开始用户验证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从token中获取用户名
        String username = token.getUsername();
        //根据用户名去数据库中查询结果
        String password = adminUserDao.getpasswordByUsername(username);
        //如果查不到说明没有该用户，如果不一致说明密码错误，抛出异常
        if(password == null || !password.equals(new String(token.getPassword()))){
            throw new AuthenticationException("用户名或密码错误");
        }
        //没有抛出异常，返回结果
        //组合一个验证信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(),password,getName());
        return info;
    }
}
