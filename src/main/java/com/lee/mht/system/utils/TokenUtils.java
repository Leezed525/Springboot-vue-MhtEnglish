package com.lee.mht.system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author FucXing
 * @date 2021/12/23 17:00
 **/
@Slf4j
public class TokenUtils {

    //加密的密钥
    private static final String secret = "LEEMHT";
    //获取token的key，一般token存在请求头和响应投中
    public static final String tokenHead = "mhtTokenHead";
    //token有效时间
    private static final Long expTime = 24*60*60*1000L;

    public static String getToken(String username,String password){
        JwtBuilder builder = Jwts.builder();
        //设置加密方式
        builder.signWith(SignatureAlgorithm.HS256,secret)
                //设置关键信息
                .setSubject(username)
                //防止密码（暂不确定能不能取就不放了）
                //.setSubject(password)
                //设置签发时间
                .setIssuedAt(new Date())
                //设置有效时间
                .setExpiration(new Date(System.currentTimeMillis() + expTime));
        //生成
        String token = builder.compact();
        log.info("token = " + token);
        return token;
    }

    /**
     * 查看并解析token
     * 这个方法会在token异常的时候自动抛出异常,不用自定异常,
     * 只需要在验证的时候进行捕获即可
     * @param token
     * @return
     */
    public static Claims getTokenBody(String token){
        //这里得到是token中的载荷部分,也是具体信息的所在
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public static String getUsername(String token) {
        String username = getTokenBody(token).getSubject();
        log.info(username);
        return username;
    }

}
