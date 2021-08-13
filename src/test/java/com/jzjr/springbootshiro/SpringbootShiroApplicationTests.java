package com.jzjr.springbootshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jzjr.springbootshiro.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringbootShiroApplicationTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
    }

    @Test
    public void initRedisData() {
        stringRedisTemplate.opsForValue().set("1720000000","435466",30, TimeUnit.MINUTES);
        String phoneNumber = stringRedisTemplate.opsForValue().get("1720000000");
        System.out.println(phoneNumber);
    }
}
