/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.login.repository;

import com.example.login.entity.User;
import com.example.login.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPermissionRepository
        extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUser(User user);

List<UserPermission> findByUserId(Long userId);

    Optional<UserPermission> findByUserAndFeature(
            User user,
            String feature
    );
}