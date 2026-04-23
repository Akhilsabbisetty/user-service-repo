package com.ascloud;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
@RestController
public class UserApp {

    @Autowired
    private StringRedisTemplate redis;

    @GetMapping("/users")
    public String users() {
        return "User Service Running";
    }

    @GetMapping("/cache")
    public String cache() {
        redis.opsForValue().set("user", "akhil");
        return redis.opsForValue().get("user");
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}