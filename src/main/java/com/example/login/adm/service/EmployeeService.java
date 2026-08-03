package com.example.login.adm.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.login.adm.entity.Employee;
import com.example.login.adm.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true, transactionManager = "admTransactionManager")
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true, transactionManager = "admTransactionManager")
    public List<Employee> findByFilters(String nik, String name, String factory, String status) {
        return employeeRepository.findAll().stream()
            .filter(employee -> containsIgnoreCase(employee.getEmpnik(), nik))
            .filter(employee -> containsIgnoreCase(employee.getEmpname(), name))
            .filter(employee -> equalsIgnoreCase(employee.getFctnam(), factory))
            .filter(employee -> equalsIgnoreCase(employee.getEmpsts(), status))
            .toList();
    }

    @Transactional(readOnly = true, transactionManager = "admTransactionManager")
    public List<String> findDistinctFactories() {
        return employeeRepository.findAll().stream()
            .map(Employee::getFctnam)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();
    }

    @Transactional(readOnly = true, transactionManager = "admTransactionManager")
    public List<String> findDistinctStatuses() {
        return employeeRepository.findAll().stream()
            .map(Employee::getEmpsts)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();
    }

    private boolean containsIgnoreCase(String source, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        if (source == null) {
            return false;
        }
        String normalizedSource = source.toLowerCase(Locale.ROOT);
        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        return normalizedSource.contains(normalizedQuery);
    }

    private boolean equalsIgnoreCase(String source, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        if (source == null) {
            return false;
        }
        return source.trim().equalsIgnoreCase(query.trim());
    }
}