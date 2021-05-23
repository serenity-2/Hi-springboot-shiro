package com.jzjr.springbootshiro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class SpringbootShiroApplicationTests {

    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        }
    }

}
