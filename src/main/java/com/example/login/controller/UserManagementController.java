package com.example.login.controller;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserManagementController {

    private final UserRepository userRepository;

    public UserManagementController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================
    // DAFTAR USER
    // =========================

    @GetMapping("/users")
    public String users(
            HttpSession session,
            Model model) {

        Object username = session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        Object role = session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/home";
        }

        model.addAttribute("username", username);
        model.addAttribute("users", userRepository.findAll());

        return "users";
    }


    // =========================
    // FORM TAMBAH USER
    // =========================

    @GetMapping("/users/new")
    public String newUserForm(
            HttpSession session,
            Model model) {

        Object username = session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        Object role = session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/home";
        }

        model.addAttribute("user", new User());

        return "user-form";
    }


    // =========================
    // SIMPAN USER
    // =========================

   @PostMapping("/users/save")
public String saveUser(
        @ModelAttribute("user") User user,
        HttpSession session,
        Model model) {

    Object username = session.getAttribute("username");

    if (username == null) {
        return "redirect:/login";
    }

    Object role = session.getAttribute("role");

    if (!"ADMIN".equals(role)) {
        return "redirect:/home";
    }

    // =========================
    // CEK USERNAME
    // =========================

    if (user.getUsername() == null ||
        user.getUsername().trim().isEmpty()) {

        model.addAttribute(
            "error",
            "Username wajib diisi."
        );

        return "user-form";
    }

    // =========================
    // CEK USERNAME SUDAH ADA
    // =========================

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

    // =========================
    // GENERATE ID
    // =========================

    Long maxId = userRepository.findMaxId();

    if (maxId == null) {
        user.setId(1L);
    } else {
        user.setId(maxId + 1);
    }

    // =========================
    // TANGGAL DIBUAT
    // =========================

    user.setCreatedAt(new java.util.Date());

    // =========================
    // ROLE DEFAULT
    // =========================

    if (user.getRole() == null ||
        user.getRole().trim().isEmpty()) {

        user.setRole("USER");
    }

    // =========================
    // SIMPAN USER
    // =========================

    userRepository.save(user);

    return "redirect:/users";
}
}