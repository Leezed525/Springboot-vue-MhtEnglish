package com.lee.mht.system.shiro;

import com.lee.mht.system.utils.JwtUtils;
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
        //return token;
        return JwtUtils.getUserId(token);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
