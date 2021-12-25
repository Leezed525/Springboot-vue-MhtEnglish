package com.lee.mht.system.shiro;

import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.utils.TokenUtils;
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
        String token = (String) authenticationToken.getCredentials();
        //验证token 有效性
        String username = TokenUtils.getUsername(token);
        log.info(username);
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("token非法无效");
        }
        AdminUser user = adminUserDao.getAdminUserByUsername(username);
        //判断用户是否存在
        if(user == null){
            throw new AuthenticationException("用户不存在");
        }
        //判断用户是否可用
        if(!user.getAvailable()){
            throw new AuthenticationException("用户已被禁用");
        }
        return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
    }
}
