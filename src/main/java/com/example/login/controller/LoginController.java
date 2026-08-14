package com.example.login.controller;

import com.example.login.entity.User;
import com.example.login.service.AuthService;
import com.example.login.model.LoginForm;

import javax.validation.Valid;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping({"/", "/login"})
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(
            @Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        // LOGIN
        User user = authService.authenticate(
                loginForm.getUsername(),
                loginForm.getPassword()
        );

        // LOGIN GAGAL
        if (user == null) {
            model.addAttribute(
                    "loginError",
                    "Username atau password salah"
            );
            return "login";
        }

        // LOGIN BERHASIL
        session.setAttribute(
                "username",
                user.getUsername()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(
            HttpSession session,
            Model model) {

        Object user =
                session.getAttribute("username");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "username",
                user.toString()
        );

        model.addAttribute(
                "role",
                session.getAttribute("role")
        );

        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}