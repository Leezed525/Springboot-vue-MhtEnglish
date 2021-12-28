package com.lee.mht.system.shiro;

import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author FucXing
 * @date 2021/12/28 00:44
 **/
@Slf4j
public class LeeMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        //检验token是否合法
        JwtToken jwtToken = (JwtToken) token;
        //log.info(jwtToken.toString());
        try{
            return JwtUtils.validateToken(String.valueOf(jwtToken.getCredentials()));
        }catch(Exception e){
            throw new AuthenticationException("非法Token,请登出后重新登录");
        }
    }
}
