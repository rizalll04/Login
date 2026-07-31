package com.example.login.it.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.login.it.entity.ItTaskMember;
import com.example.login.it.service.ItTaskMemberService;

@Controller
public class ItTaskMemberController {

    private final ItTaskMemberService itTaskMemberService;

    public ItTaskMemberController(ItTaskMemberService itTaskMemberService) {
        this.itTaskMemberService = itTaskMemberService;
    }

    @GetMapping("/task")
    public String taskList(HttpSession session, Model model) {
        Object user = session.getAttribute("username");
        if (user == null) {
            return "redirect:/login";
        }

        List<ItTaskMember> tasks = itTaskMemberService.findAll();
        model.addAttribute("username", user.toString());
        model.addAttribute("tasks", tasks);
        return "dailytaks";
    }
}