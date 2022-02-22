package com.lee.mht.system.shiro;

import com.lee.mht.business.service.UserService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.exception.SystemException;
import com.lee.mht.system.service.AdminPermissionService;
import com.lee.mht.system.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/23 12:53
 **/
@Slf4j
@Component
public class LeeRealm extends AuthorizingRealm {


    @Autowired
    @Lazy
    private AdminPermissionService adminPermissionService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("开始用户授权");
        int id = Integer.parseInt((String) principalCollection.getPrimaryPrincipal());
        //创建返回的info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (id == 1) {
            info.addStringPermission("*:*");
        } else {
            //查询用户的所有权限
            List<String> permissions = adminPermissionService.getAllPermissionByUserId(id);
            if (permissions != null) {
                info.addStringPermissions(permissions);
            }
        }
        return info;
    }

    //用户登入
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("开始用户验证");
        JwtToken token = (JwtToken) authenticationToken;
        //验证toekn合法
        try {
            Object principal = token.getPrincipal();
            Object credential = token.getCredentials();
            return new SimpleAuthenticationInfo(principal,credential, getName());

        } catch (Exception e) {
            throw new SystemException(Constant.TOKEN_ERROR_CODE, Constant.ILLEAGEL_TOKEN);
        }
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
