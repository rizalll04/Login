package com.example.login.adm.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.login.adm.entity.Employee;
import com.example.login.adm.service.EmployeeService;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public String employeeList(HttpSession session, Model model) {
        Object user = session.getAttribute("username");
        if (user == null) {
            return "redirect:/login";
        }

        List<Employee> employees = employeeService.findAll();
        model.addAttribute("username", user.toString());
        model.addAttribute("employees", employees);
        return "datakaryawan";
    }
}