package com.example.login.controller;

import com.example.login.entity.User;
import com.example.login.model.LoginForm;
import com.example.login.service.AuthService;
import com.example.login.service.PermissionService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthService authService;
    private final PermissionService permissionService;

    public LoginController(
            AuthService authService,
            PermissionService permissionService) {

        this.authService = authService;
        this.permissionService = permissionService;
    }

    // =========================
    // LOGIN PAGE
    // =========================

    @GetMapping({"/", "/login"})
    public String loginForm(Model model) {

        model.addAttribute(
                "loginForm",
                new LoginForm()
        );

        return "login";
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public String loginSubmit(
            @Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        // =========================
        // CEK LOGIN
        // =========================

        User user = authService.authenticate(
                loginForm.getUsername(),
                loginForm.getPassword()
        );

        // =========================
        // LOGIN GAGAL
        // =========================

        if (user == null) {

            model.addAttribute(
                    "loginError",
                    "Username atau password salah"
            );

            return "login";
        }

        // =========================
// SIMPAN SESSION
// =========================

session.setAttribute(
        "userId",
        user.getId()
);

session.setAttribute(
        "username",
        user.getUsername()
);

session.setAttribute(
        "role",
        user.getRole()
);

// =========================
// SIMPAN PERMISSION
// =========================

session.setAttribute(
        "permissionDashboard",
        permissionService.hasPermission(
                user,
                "DASHBOARD"
        )
);

session.setAttribute(
        "permissionEmployee",
        permissionService.hasPermission(
                user,
                "EMPLOYEE"
        )
);

session.setAttribute(
        "permissionTask",
        permissionService.hasPermission(
                user,
                "TASK"
        )
);

session.setAttribute(
        "permissionUserManagement",
        permissionService.hasPermission(
                user,
                "USER_MANAGEMENT"
        )
);

        // =========================
        // MASUK HOME
        // =========================

        return "redirect:/home";
    }

    // =========================
    // HOME
    // =========================

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

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}