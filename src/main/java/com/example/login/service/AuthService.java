package com.example.login.service;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${app.init-default-users:false}")
    private boolean initDefaultUsersFlag;

    @PostConstruct
    public void initDefaultUsers() {
        if (!initDefaultUsersFlag) return;
        if (userRepository.count() == 0) {
            userRepository.save(new User("user", "password"));
            userRepository.save(new User("admin", "admin"));
        }
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        Optional<User> u = userRepository.findByUsername(username);
        return u.isPresent() && password.equals(u.get().getPassword());
    }
}
