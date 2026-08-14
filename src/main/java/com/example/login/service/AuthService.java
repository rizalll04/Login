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

        if (!initDefaultUsersFlag) {
            return;
        }

        // Jangan membuat User baru di sini dulu.
        // User sudah tersedia di database.
    }

    public User authenticate(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        Optional<User> optionalUser =
                userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (password.equals(user.getPassword())) {
                return user;
            }
        }

        return null;
    }
}