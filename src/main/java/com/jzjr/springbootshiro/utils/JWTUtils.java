package com.jzjr.springbootshiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JWTUtils {
    public static final long EXPITE_TIME = 5 * 60 * 1000;
    public static final String SECRET = "secret";
    /**
     * 生成签名,expireTime后过期
     *
     * @param userName
     * @param secret
     * @return
     */
    public static String sign(String userName, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPITE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("username", userName)
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     * @param userName
     * @param secret
     * @param token
     * @return
     */
    public static boolean verify(String token, String userName, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withClaim("username", userName).build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取token中的基本信息用户名(无需解密）
     *
     * @param token
     * @return
     */
    public static String getUserName(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("username").asString();
    }

    /**
     * 获取token签发时间
     * @param token
     * @return
     */
    public static Date getIssueAt(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getIssuedAt();
    }
    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt().before(now);
    }

}
