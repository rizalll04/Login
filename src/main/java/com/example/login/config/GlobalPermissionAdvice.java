/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.login.config;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.PermissionService;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalPermissionAdvice {

    private final UserRepository userRepository;
    private final PermissionService permissionService;

    public GlobalPermissionAdvice(
            UserRepository userRepository,
            PermissionService permissionService) {

        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    @ModelAttribute("canAccessUsers")
    public boolean canAccessUsers(
            HttpSession session) {

        Object userId =
                session.getAttribute("userId");

        if (userId == null) {
            return false;
        }

        User user =
                userRepository.findById(
                        Long.valueOf(userId.toString())
                ).orElse(null);

        if (user == null) {
            return false;
        }

        return permissionService.hasPermission(
                user,
                "USERS"
        );
    }
}