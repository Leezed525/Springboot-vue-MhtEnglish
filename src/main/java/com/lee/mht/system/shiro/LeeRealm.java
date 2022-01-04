package com.lee.mht.system.shiro;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("开始用户授权");
        String accessToken = (String) principalCollection.getPrimaryPrincipal();
        Integer id = Integer.valueOf(JwtUtils.getUserId(accessToken));

        //创建返回的info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (id == 1) {
            info.addStringPermission("*:*");
        } else {
            //先去redis里找，找不到再去claims里找


            //以上留空为redis做准备


            Claims claims = JwtUtils.getClaimsFromToken(accessToken);
            List<String> permissions = (List<String>) claims.get(Constant.JWT_PERMISSIONS_KEY);
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
        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
    }
}
