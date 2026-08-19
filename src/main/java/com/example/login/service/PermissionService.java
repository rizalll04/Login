package com.example.login.service;

import com.example.login.entity.User;
import com.example.login.entity.UserPermission;
import com.example.login.repository.UserPermissionRepository;
import com.example.login.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final UserPermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public PermissionService(
            UserPermissionRepository permissionRepository,
            UserRepository userRepository) {

        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    // =====================================================
    // CEK PERMISSION USER
    // =====================================================

    public boolean hasPermission(
            User user,
            String feature) {

        if (user == null || feature == null) {
            return false;
        }

        // ADMIN SELALU BOLEH SEMUA
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return true;
        }

        Optional<UserPermission> permission =
                permissionRepository.findByUserAndFeature(
                        user,
                        feature.toUpperCase()
                );

        return permission.isPresent()
                && permission.get().isAllowed();
    }

    // =====================================================
    // CEK PERMISSION BERDASARKAN USERNAME
    // =====================================================

    public boolean hasPermission(
            String username,
            String feature) {

        if (username == null || feature == null) {
            return false;
        }

        Optional<User> user =
                userRepository.findByUsername(username);

        if (!user.isPresent()) {
            return false;
        }

        return hasPermission(user.get(), feature);
    }

    // =====================================================
    // AMBIL SEMUA PERMISSION USER
    // =====================================================

    public List<UserPermission> getUserPermissions(
            Long userId) {

        return permissionRepository.findByUserId(userId);
    }
// =========================
// BUAT DEFAULT PERMISSION
// =========================

public void createDefaultPermissions(User user) {

    String[] features = {
        "DASHBOARD",
        "EMPLOYEE",
        "TASK",
        "USER_MANAGEMENT"
    };

    for (String feature : features) {

        Optional<UserPermission> existing =
                permissionRepository.findByUserAndFeature(
                        user,
                        feature
                );

        if (!existing.isPresent()) {

            UserPermission permission =
                    new UserPermission();

            permission.setUser(user);
            permission.setFeature(feature);
            permission.setEnabled(0);

            permissionRepository.save(permission);
        }
    }
}
    // =====================================================
    // UPDATE PERMISSION
    // =====================================================

    public void updatePermissions(
            Long userId,
            List<String> enabledFeatures) {

        List<UserPermission> permissions =
                permissionRepository.findByUserId(userId);

        if (enabledFeatures == null) {
            enabledFeatures = new ArrayList<>();
        }

        for (UserPermission permission : permissions) {

            String feature =
                    permission.getFeature();

            if (enabledFeatures.contains(feature)) {
                permission.setEnabled(1);
            } else {
                permission.setEnabled(0);
            }
        }

        permissionRepository.saveAll(permissions);
    }
}