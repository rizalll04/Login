package com.example.login.it.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.login.it.entity.ItTaskMember;

public interface ItTaskMemberRepository extends JpaRepository<ItTaskMember, Long> {

    List<ItTaskMember> findByEmpnik(String empnik);
}