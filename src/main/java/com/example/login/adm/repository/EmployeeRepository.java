package com.example.login.adm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.login.adm.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

	List<Employee> findByDptkod(String dptkod);
}
