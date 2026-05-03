package com.ascloud;

import jakarta.persistence.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class UserApp {

    private final UserRepository userRepository;

    public UserApp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Map<String, String> status() {
        return Map.of("service", "user-service", "status", "running");
    }

    @PostMapping("/users/signup")
    public Map<String, Object> signup(@RequestBody SignupRequest request) {
        if (request.email == null || request.email.isBlank()) {
            return Map.of("status", "failed", "message", "Email is required");
        }

        Optional<SneakHiveUser> existing = userRepository.findByEmail(request.email);

        if (existing.isPresent()) {
            return Map.of("status", "failed", "message", "User already exists");
        }

        SneakHiveUser user = new SneakHiveUser();
        user.name = request.name;
        user.email = request.email;
        user.password = request.password;

        userRepository.save(user);

        return Map.of(
                "status", "success",
                "message", "Account created successfully",
                "userId", user.id,
                "name", user.name,
                "email", user.email
        );
    }

    @PostMapping("/users/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        Optional<SneakHiveUser> user = userRepository.findByEmailAndPassword(
                request.email,
                request.password
        );

        if (user.isEmpty()) {
            return Map.of("status", "failed", "message", "Invalid email or password");
        }

        SneakHiveUser u = user.get();

        return Map.of(
                "status", "success",
                "message", "Login successful",
                "userId", u.id,
                "name", u.name,
                "email", u.email
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}

@Entity
@Table(name = "users")
class SneakHiveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    @Column(unique = true)
    public String email;

    public String password;
}

interface UserRepository extends JpaRepository<SneakHiveUser, Long> {
    Optional<SneakHiveUser> findByEmail(String email);
    Optional<SneakHiveUser> findByEmailAndPassword(String email, String password);
}

class SignupRequest {
    public String name;
    public String email;
    public String password;
}

class LoginRequest {
    public String email;
    public String password;
}
