package com.example.login.it.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    @Transactional(readOnly = true, transactionManager = "itTransactionManager")
    public List<ItTaskMember> findByFilters(String nik, String factory, String status) {
        return itTaskMemberRepository.findAll().stream()
            .filter(task -> containsIgnoreCase(task.getEmpnik(), nik))
            .filter(task -> equalsIgnoreCase(task.getTskfct(), factory))
            .filter(task -> equalsIgnoreCase(task.getTsksts(), status))
            .toList();
    }

    @Transactional(readOnly = true, transactionManager = "itTransactionManager")
    public List<String> findDistinctFactories() {
        return itTaskMemberRepository.findAll().stream()
            .map(ItTaskMember::getTskfct)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();
    }

    @Transactional(readOnly = true, transactionManager = "itTransactionManager")
    public List<String> findDistinctStatuses() {
        return itTaskMemberRepository.findAll().stream()
            .map(ItTaskMember::getTsksts)
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