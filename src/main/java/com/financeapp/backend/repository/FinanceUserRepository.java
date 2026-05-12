package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceUserRepository extends JpaRepository<FinanceUser, Long> {
    public FinanceUser findByEmail(String email);

    public boolean existsByEmail(String email);

}