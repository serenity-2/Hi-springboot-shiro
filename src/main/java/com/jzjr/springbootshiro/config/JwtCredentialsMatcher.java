package com.jzjr.springbootshiro.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jzjr.springbootshiro.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

@Slf4j
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = (String) authenticationToken.getCredentials();
        Object stored = authenticationInfo.getCredentials();
        String salt = stored.toString();
        User user = (User) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", user.getUserName())
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("Token Error:{}", e.getMessage());
        }
        return false;
    }
}
