package com.example.login.controller;

import com.example.login.entity.User;
import com.example.login.entity.UserPermission;
import com.example.login.repository.UserRepository;
import com.example.login.service.PermissionService;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserManagementController {

    private final UserRepository userRepository;
    private final PermissionService permissionService;

    public UserManagementController(
            UserRepository userRepository,
            PermissionService permissionService) {

        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    // =====================================================
    // CEK LOGIN
    // =====================================================

    private User getCurrentUser(HttpSession session) {

        Object userId = session.getAttribute("userId");

        if (userId == null) {
            return null;
        }

        try {

            return userRepository.findById(
                    Long.valueOf(userId.toString())
            ).orElse(null);

        } catch (Exception e) {

            return null;
        }
    }

    // =====================================================
    // CEK AKSES USER MANAGEMENT
    // =====================================================

    private boolean canManageUsers(HttpSession session) {

        User user = getCurrentUser(session);

        if (user == null) {
            return false;
        }

        return permissionService.hasPermission(
                user,
                "USER_MANAGEMENT"
        );
    }

    // =====================================================
    // DAFTAR USER
    // =====================================================

    @GetMapping("/users")
    public String users(
            HttpSession session,
            Model model) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        // ADMIN = otomatis boleh
        // USER = harus USER_MANAGEMENT ON
        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        model.addAttribute(
                "username",
                currentUser.getUsername()
        );

        model.addAttribute(
                "users",
                userRepository.findAll()
        );

        return "users";
    }

    // =====================================================
    // FORM TAMBAH USER
    // =====================================================

    @GetMapping("/users/new")
    public String newUserForm(
            HttpSession session,
            Model model) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        User user = new User();

        model.addAttribute(
                "user",
                user
        );

        // ================================================
        // PERMISSION UNTUK USER BARU
        // ================================================

        List<UserPermission> permissions =
                new java.util.ArrayList<>();

        String[] features = {
            "DASHBOARD",
            "EMPLOYEE",
            "TASK",
            "USER_MANAGEMENT"
        };

        for (String feature : features) {

            UserPermission permission =
                    new UserPermission();

            permission.setFeature(feature);
            permission.setEnabled(0);

            permissions.add(permission);
        }

        model.addAttribute(
                "permissions",
                permissions
        );

        return "user-form";
    }

    // =====================================================
    // FORM EDIT USER
    // =====================================================

    @GetMapping("/users/edit/{id}")
    public String editUserForm(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        Optional<User> existingUser =
                userRepository.findById(id);

        if (!existingUser.isPresent()) {
            return "redirect:/users";
        }

        User user =
                existingUser.get();

        model.addAttribute(
                "user",
                user
        );

        List<UserPermission> permissions =
                permissionService.getUserPermissions(id);

        model.addAttribute(
                "permissions",
                permissions
        );

        return "user-form";
    }

    // =====================================================
    // UPDATE USER
    // =====================================================

    @PostMapping("/users/update")
    public String updateUser(
            @ModelAttribute("user") User updatedUser,

            @RequestParam(
                    value = "enabledFeatures",
                    required = false
            )
            List<String> enabledFeatures,

            HttpSession session,
            Model model) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        Optional<User> existingUser =
                userRepository.findById(
                        updatedUser.getId()
                );

        if (!existingUser.isPresent()) {
            return "redirect:/users";
        }

        User user =
                existingUser.get();

        // ================================================
        // UPDATE DATA USER
        // ================================================

        user.setFullname(
                updatedUser.getFullname()
        );

        user.setUsername(
                updatedUser.getUsername()
        );

        user.setEmail(
                updatedUser.getEmail()
        );

        user.setRole(
                updatedUser.getRole()
        );

        // ================================================
        // PASSWORD
        // ================================================

        if (updatedUser.getPassword() != null
                && !updatedUser.getPassword()
                .trim()
                .isEmpty()) {

            user.setPassword(
                    updatedUser.getPassword()
            );
        }

        userRepository.save(user);

        // ================================================
        // UPDATE PERMISSION
        // ================================================

        permissionService.updatePermissions(
                user.getId(),
                enabledFeatures
        );

        return "redirect:/users";
    }

    // =====================================================
    // HAPUS USER
    // =====================================================

    @GetMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            HttpSession session) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        Optional<User> user =
                userRepository.findById(id);

        if (user.isPresent()) {

            String currentUsername =
                    currentUser.getUsername();

            if (!user.get()
                    .getUsername()
                    .equals(currentUsername)) {

                userRepository.deleteById(id);
            }
        }

        return "redirect:/users";
    }

    // =====================================================
    // SIMPAN USER BARU
    // =====================================================

    @PostMapping("/users/save")
    public String saveUser(
            @ModelAttribute("user") User user,

            @RequestParam(
                    value = "enabledFeatures",
                    required = false
            )
            List<String> enabledFeatures,

            HttpSession session,
            Model model) {

        User currentUser =
                getCurrentUser(session);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!permissionService.hasPermission(
                currentUser,
                "USER_MANAGEMENT")) {

            return "redirect:/home";
        }

        // ================================================
        // CEK USERNAME
        // ================================================

        if (user.getUsername() == null
                || user.getUsername()
                .trim()
                .isEmpty()) {

            model.addAttribute(
                    "error",
                    "Username wajib diisi."
            );

            return "user-form";
        }

        // ================================================
        // CEK USERNAME SUDAH ADA
        // ================================================

        Optional<User> existingUser =
                userRepository.findByUsername(
                        user.getUsername()
                );

        if (existingUser.isPresent()) {

            model.addAttribute(
                    "error",
                    "Username sudah digunakan."
            );

            return "user-form";
        }

        // ================================================
        // GENERATE ID
        // ================================================

        Long maxId =
                userRepository.findMaxId();

        if (maxId == null) {

            user.setId(1L);

        } else {

            user.setId(
                    maxId + 1
            );
        }

        // ================================================
        // CREATED AT
        // ================================================

        user.setCreatedAt(
                new java.util.Date()
        );

        // ================================================
        // ROLE DEFAULT
        // ================================================

        if (user.getRole() == null
                || user.getRole()
                .trim()
                .isEmpty()) {

            user.setRole("USER");
        }

        // ================================================
        // SIMPAN USER
        // ================================================

        userRepository.save(user);

        // ================================================
        // SIMPAN PERMISSION
        // ================================================

        // Pastikan permission untuk user baru
        // dibuat terlebih dahulu.
        permissionService.createDefaultPermissions(
                user
        );

        // Setelah dibuat, update sesuai checkbox
        permissionService.updatePermissions(
                user.getId(),
                enabledFeatures
        );

        return "redirect:/users";
    }
}
