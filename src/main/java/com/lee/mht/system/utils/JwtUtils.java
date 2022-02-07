package com.lee.mht.system.utils;

import com.lee.mht.system.common.Constant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2021/12/25 02:08
 **/
@Slf4j
public class JwtUtils {

    private static final String issuer = "LeeZed";
    private static final String secretKey = "FucXingMht"; // token 密钥

    private static final Long accessTokenExpireTime = 1000 * 60 * 5L;//accessToken (发给客户端的5分钟过期一次)
    //private static final Long refreshTokenExpireTime = 1000 * 60 * 60 * 24L; //refreshToken(保留再redis的一天过期一次)

    //生成MHT业务token
    public static String generateMhtToken(String id,String username,String userType){
        long nowTimestamp = new Timestamp(System.currentTimeMillis()).getTime();
        Map<String, Object> claims = new HashMap<>();
        //放入用户名
        claims.put(Constant.JWT_USER_NAME, username);
        //签发时间
        claims.put(Constant.JWT_ISSUANCE_TIME,nowTimestamp);
        //存放类型
        claims.put(Constant.JWT_USER_TYPE,userType);
        //accessToken 中传入adminuser的id和claims
        return JwtUtils.getAccessToken(id, claims);
    }

    /**
     * 生成 access_token
     */
    public static String getAccessToken(String subject, Map<String, Object> claims) {

        return generateToken(issuer, subject, claims, accessTokenExpireTime, secretKey);
    }

    /*
     * 生成refreshtoken
     * */
    //public static String getRefreshToken(String subject, Map<String, Object> claims) {
    //    return generateToken(issuer, subject, claims, refreshTokenExpireTime, secretKey);
    //}

    /**
     * 签发token
     */
    public static String generateToken(String issuer, String subject,
                                       Map<String, Object> claims, long ttlMillis, String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam("typ", "JWT");
        if (null != claims) {
            builder.setClaims(claims);
        }
        //if (!StringUtils.isEmpty(subject)) {//这个方法以弃用
        if (StringUtils.hasLength(subject)) {
            builder.setSubject(subject);
        }
        //if (!StringUtils.isEmpty(issuer)) {
        if (StringUtils.hasLength(issuer)) {
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

    /**
     * 获取用户id 从token中
     */
    public static String getId(String token) {
        String userId = null;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("error={}", e);
        }
        return userId;
    }

    /**
     * 从request中获取token在获取id
     * @param request request
     * @return id
     */
    public static int getId(HttpServletRequest request){
        String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
        return Integer.parseInt(JwtUtils.getId(token));
    }

    /**
     * 从令牌中获取数据声明
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims=null;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            if(e instanceof ClaimJwtException){
                claims=((ClaimJwtException) e).getClaims();
            }
        }
        return claims;
    }

    /**
     * 从claim中获取用户名
     */
    public static String getUserName(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(Constant.JWT_USER_NAME);
        } catch (Exception e) {
            log.error("error={}", e);
        }
        return username;
    }

    /**
     * 从claim中获取用户类型
     */
    public static String getUserType(String token) {
        String type = null;
        try {
            Claims claims = getClaimsFromToken(token);
            type = (String) claims.get(Constant.JWT_USER_TYPE);
        } catch (Exception e) {
            log.error("error={}", e);
        }
        return type;
    }

    /**
     * 校验令牌是否被篡改
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return null != claimsFromToken;
    }
}