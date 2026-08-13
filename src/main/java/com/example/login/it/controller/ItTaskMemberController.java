package com.example.login.it.controller;

import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.login.it.entity.ItTaskMember;
import com.example.login.it.service.ItTaskMemberService;

@Controller
public class ItTaskMemberController {

    private final ItTaskMemberService itTaskMemberService;

    public ItTaskMemberController(ItTaskMemberService itTaskMemberService) {
        this.itTaskMemberService = itTaskMemberService;
    }

    @GetMapping("/task")
    public String taskList(
        HttpSession session,
        Model model,
        @RequestParam(required = false) String nik,
        @RequestParam(required = false) String factory,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Object user = session.getAttribute("username");
        if (user == null) {
            return "redirect:/login";
        }

        int pageSize = normalizePageSize(size);
        boolean hasFilter = hasText(nik) || hasText(factory) || hasText(status);
        List<ItTaskMember> filteredTasks = hasFilter
            ? itTaskMemberService.findByFilters(nik, factory, status)
            : itTaskMemberService.findAll();

        int totalItems = filteredTasks.size();
        int totalPages = totalItems == 0 ? 0 : (int) Math.ceil((double) totalItems / pageSize);
        int currentPage = totalPages == 0 ? 0 : Math.max(0, Math.min(page, totalPages - 1));
        List<ItTaskMember> tasks = paginate(filteredTasks, currentPage, pageSize);

        model.addAttribute("username", user.toString());
        model.addAttribute("tasks", tasks);
        model.addAttribute("hasFilter", hasFilter);
        model.addAttribute("nik", Objects.toString(nik, ""));
        model.addAttribute("factory", Objects.toString(factory, ""));
        model.addAttribute("status", Objects.toString(status, ""));
        model.addAttribute("factoryOptions", itTaskMemberService.findDistinctFactories());
        model.addAttribute("statusOptions", itTaskMemberService.findDistinctStatuses());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "dailytaks";
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private int normalizePageSize(int requestedSize) {
        List<Integer> allowedSizes = Arrays.asList(10, 25, 50, 100);
        return allowedSizes.contains(requestedSize) ? requestedSize : 10;
    }

    private <T> List<T> paginate(List<T> source, int page, int size) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, source.size());
        if (fromIndex >= source.size() || fromIndex < 0) {
            return Collections.emptyList();
        }
        return source.subList(fromIndex, toIndex);
    }
}