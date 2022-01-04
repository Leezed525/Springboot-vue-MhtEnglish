package com.lee.mht.system.shiro;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.exception.SystemException;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
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
        String accessToken= (String) token.getPrincipal();
        String userId= JwtUtils.getUserId(accessToken);
        //检验token是否合法
        try{
            if(!JwtUtils.validateToken(accessToken)){
                throw new SystemException(Constant.TOKEN_ERROR,Constant.ILLEAGEL_TOKEN);
            }
        }catch(Exception e){
            throw new SystemException(Constant.TOKEN_ERROR,Constant.ILLEAGEL_TOKEN);
        }
        //判断token是否过期
        if(!redisUtils.hasKey(Constant.REDIS_TOKEN_KEY + userId)){//过期了
            throw new SystemException(Constant.TOKEN_ERROR,Constant.TOKEN_EXPIRED);
        }else{
            //没过期刷新token延长时间
            redisUtils.set(Constant.REDIS_TOKEN_KEY + userId,accessToken,Constant.TOKEN_EXPIRE_TIME);
        }
        return true;
    }
}
