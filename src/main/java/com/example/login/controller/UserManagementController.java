package com.example.login.controller;

import com.example.login.entity.User;
import com.example.login.repository.UserRepository;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
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
// FORM EDIT USER
// =========================

@GetMapping("/users/edit/{id}")
public String editUserForm(
        @PathVariable Long id,
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

    Optional<User> user =
            userRepository.findById(id);

 if (!user.isPresent())  {
        return "redirect:/users";
    }

    model.addAttribute("user", user.get());

    return "user-form";
}
// =========================
// UPDATE USER
// =========================

@PostMapping("/users/update")
public String updateUser(
        @ModelAttribute("user") User updatedUser,
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

    Optional<User> existingUser =
            userRepository.findById(updatedUser.getId());

   if (!existingUser.isPresent())  {
        return "redirect:/users";
    }

    User user = existingUser.get();

    // Update data
    user.setFullname(updatedUser.getFullname());
    user.setUsername(updatedUser.getUsername());
    user.setEmail(updatedUser.getEmail());
    user.setRole(updatedUser.getRole());

    // Password hanya diubah kalau diisi
    if (updatedUser.getPassword() != null &&
        !updatedUser.getPassword().trim().isEmpty()) {

        user.setPassword(updatedUser.getPassword());
    }

    // Created_at jangan diubah
    userRepository.save(user);

    return "redirect:/users";
}
// =========================
// HAPUS USER
// =========================

@GetMapping("/users/delete/{id}")
public String deleteUser(
        @PathVariable Long id,
        HttpSession session) {

    Object username =
            session.getAttribute("username");

    if (username == null) {
        return "redirect:/login";
    }

    Object role =
            session.getAttribute("role");

    if (!"ADMIN".equals(role)) {
        return "redirect:/home";
    }

    Optional<User> user =
            userRepository.findById(id);

    if (user.isPresent()) {

        // Jangan izinkan admin menghapus dirinya sendiri
        if (!user.get()
                .getUsername()
                .equals(username)) {

            userRepository.deleteById(id);
        }
    }

    return "redirect:/users";
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