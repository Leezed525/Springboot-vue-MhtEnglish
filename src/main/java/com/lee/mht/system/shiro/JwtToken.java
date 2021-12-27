package com.lee.mht.system.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author FucXing
 * @date 2021/12/24 17:00
 **/
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token){
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
