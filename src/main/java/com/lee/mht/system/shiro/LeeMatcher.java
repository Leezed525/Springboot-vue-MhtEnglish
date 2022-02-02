package com.lee.mht.system.shiro;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.exception.SystemException;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * @author FucXing
 * @date 2021/12/28 00:44
 **/
@Slf4j
public class LeeMatcher extends HashedCredentialsMatcher {

    @Autowired
    @Lazy
    RedisUtils redisUtils;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        String accessToken= (String) token.getCredentials();
        String id= JwtUtils.getId(accessToken);
        //检验token是否合法
        try{
            if(!JwtUtils.validateToken(accessToken)){
                throw new SystemException(Constant.TOKEN_ERROR,Constant.ILLEAGEL_TOKEN);
            }
        }catch(Exception e){
            throw new SystemException(Constant.TOKEN_ERROR,Constant.ILLEAGEL_TOKEN);
        }
        //判断用户类型
        String tokenKey;
        if (JwtUtils.getUserType(accessToken).equals(Constant.JWT_USER_TYPE_ADMIN)){
            tokenKey = Constant.REDIS_ADMIN_USER_TOKEN_KEY + id;
        }else{
            tokenKey = Constant.REDIS_BUSINESS_USER_TOKEN_KEY + id;
        }
        //判断token是否过期
        log.info(tokenKey);

        if(isTokenExpired(tokenKey,accessToken)){
            throw new SystemException(Constant.TOKEN_ERROR,Constant.TOKEN_EXPIRED);
        }
        else{
            redisUtils.set(tokenKey,accessToken,Constant.TOKEN_EXPIRE_TIME);
        }
        return true;
    }

    //判断token是否过期，如果过期了的话抛出异常，没过期刷新时长
    private boolean isTokenExpired(String tokenKey,String token){
        if(!redisUtils.hasKey(tokenKey)){//过期了
            return true;
        }else{
            return false;
        }
    }
}
