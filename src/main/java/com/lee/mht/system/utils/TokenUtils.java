package com.lee.mht.system.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    private static final Long EXPIRE_TIME = 24*60*60*1000L;

    /**
     * 校验token是否正确
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.info(jwt+":-token is valid");
            return true;
        } catch (Exception e) {
            log.info("The token is invalid{}",e.getMessage());
            return false;
        }
    }

    /**
     * 生成签名,5min(分钟)后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //用固定的密钥生成token
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }
}
