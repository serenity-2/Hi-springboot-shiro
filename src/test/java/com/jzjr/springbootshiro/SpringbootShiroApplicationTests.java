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
import java.util.*;
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
    public void initRedisData() throws InterruptedException {
//        stringRedisTemplate.opsForValue().set("17200000000","435466",30, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("phoneNumber","17200000000");
        map.put("verifyCode","432566");
        map.put("loginCount","0");
        stringRedisTemplate.opsForHash().putAll("Daisy_"+"17255555555",map);
//        stringRedisTemplate.expire("Daisy",30,TimeUnit.MINUTES);
        String phoneNumber = (String) stringRedisTemplate.opsForHash().get("Daisy","verifyCode");
        System.out.println(phoneNumber);
        Thread.sleep(3000);
        //获取key的过期时间，默认单位为秒，没有设置过期时间则返回-1，不存在key则返回-2
        Long expireTime = stringRedisTemplate.opsForHash().getOperations().getExpire("Daisy",TimeUnit.MINUTES);
        System.out.println(expireTime);
    }

    @Test
    public void initLockAccount() {
        stringRedisTemplate.opsForValue().set("LOCKED_" + "Daisy_17211111111", "locked", 20, TimeUnit.MINUTES);
    }

    @Test
    public void initSuccessAccount(){
        stringRedisTemplate.opsForValue().set("FREELOGIN_1_Daisy_17222222222","free_login", 72*60, TimeUnit.MINUTES);
    }

}
