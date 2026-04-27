package com.ascloud;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.Map;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class UserApp {

    @Autowired
    private StringRedisTemplate redis;

    @GetMapping("/users")
    public Map<String, String> users() {
        return Map.of("service", "user-service", "status", "running");
    }

    @GetMapping("/cache")
    public Map<String, String> cache() {
        redis.opsForValue().set("user", "akhil");
        return Map.of("user", redis.opsForValue().get("user"));
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}