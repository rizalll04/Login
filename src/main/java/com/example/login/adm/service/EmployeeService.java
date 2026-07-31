package com.example.login.adm.service;

import java.util.List;

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
}