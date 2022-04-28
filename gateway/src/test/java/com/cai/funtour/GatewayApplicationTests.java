package com.cai.funtour;

import com.cai.funtour.config.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = GatewayApplication.class)
class GatewayApplicationTests {

    @Autowired
    Users users;

    @Test
    void contextLoads() {
        System.out.println(users);
    }

}
