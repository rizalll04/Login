package com.example.login.it.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.login.it.entity.ItTaskMember;
import com.example.login.it.repository.ItTaskMemberRepository;

@Service
public class ItTaskMemberService {

    private final ItTaskMemberRepository itTaskMemberRepository;

    public ItTaskMemberService(ItTaskMemberRepository itTaskMemberRepository) {
        this.itTaskMemberRepository = itTaskMemberRepository;
    }

    @Transactional(readOnly = true, transactionManager = "itTransactionManager")
    public List<ItTaskMember> findAll() {
        return itTaskMemberRepository.findAll();
    }
}