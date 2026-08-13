package com.example.login.adm.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            .collect(Collectors.toList());
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
            .collect(Collectors.toList());
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
            .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String source, String query) {
        if (query == null || query.trim().isEmpty()) {
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
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        if (source == null) {
            return false;
        }
        return source.trim().equalsIgnoreCase(query.trim());
    }
    @Transactional(readOnly = true, transactionManager = "admTransactionManager")
public List<Employee> filterNewEmployees(List<Employee> employees) {

    LocalDate today = LocalDate.now();
    LocalDate batas = today.minusDays(30);

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    return employees.stream()
        .filter(employee -> {

            if (employee.getEmptmt() == null ||
                employee.getEmptmt().trim().isEmpty()) {
                return false;
            }

            try {

                LocalDate tmt = LocalDate.parse(
                    employee.getEmptmt().trim(),
                    formatter
                );

                return !tmt.isBefore(batas)
                    && !tmt.isAfter(today);

            } catch (DateTimeParseException e) {
                return false;
            }

        })
        .collect(Collectors.toList());
}
}