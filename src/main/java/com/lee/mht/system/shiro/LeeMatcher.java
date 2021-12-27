package com.lee.mht.system.shiro;

import com.lee.mht.system.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author FucXing
 * @date 2021/12/28 00:44
 **/
public class LeeMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        //检验token是否合法
        JwtToken jwtToken = (JwtToken) token;
        if(!JwtUtils.validateToken(String.valueOf(jwtToken.getCredentials()))){
            return false;
        }
        return true;
    }
}
