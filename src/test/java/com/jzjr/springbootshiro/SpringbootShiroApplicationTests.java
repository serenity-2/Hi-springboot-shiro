package com.jzjr.springbootshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jzjr.springbootshiro.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
class SpringbootShiroApplicationTests {

    @Test
    void contextLoads() {
        long expireTime = 1000*30;
        String secret = "secret";
        String userName = "daisy";
        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withClaim("username",userName)
                .withExpiresAt(date)
                .sign(algorithm);
        System.out.println(token);
    }

    @Test
    public void test(){
        String jwtToken = JWTUtils.sign("daisy", "secret");
        System.out.println(jwtToken);
        Date now = Calendar.getInstance().getTime();
        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        Date expiresAt = decodedJWT.getExpiresAt();
        Date issuedAt = decodedJWT.getIssuedAt();
        System.out.println(decodedJWT.getExpiresAt().before(now));
    }
}
