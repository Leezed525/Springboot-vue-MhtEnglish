package com.lee.mht.system.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.Map;

/**
 * @author FucXing
 * @date 2021/12/25 02:08
 **/
@Slf4j
public class JwtUtils {

    private static final String issuer = "LeeZed";
    private static final String  secretKey = "FucXingMht"; // token 密钥

    private static final Long accessTokenExpireTime = 1000*60*5L;//accessToken (发给客户端的5分钟过期一次)
    private static final Long refreshTokenExpireTime = 1000*60*60*24L; //refreshToken(保留再redis的一天过期一次)

    /**
     * 生成 access_token
     */
    public static String getAccessToken(String subject, Map<String,Object> claims){

        return generateToken(issuer,subject,claims,accessTokenExpireTime,secretKey);
    }

    /*
    * 生成refreshtoken
    * */
    public static String getRefreshToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireTime,secretKey);
    }
    /**
     * 签发token
     */
    public static String generateToken(String issuer, String subject, Map<String, Object> claims, long ttlMillis, String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam("typ","JWT");
        if(null!=claims){
            builder.setClaims(claims);
        }
        //if (!StringUtils.isEmpty(subject)) {//这个方法以弃用
        if(StringUtils.hasLength(subject)){
            builder.setSubject(subject);
        }
        //if (!StringUtils.isEmpty(issuer)) {
        if(StringUtils.hasLength(issuer)){
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }
}