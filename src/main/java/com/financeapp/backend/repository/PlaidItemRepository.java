package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaidItemRepository extends JpaRepository<PlaidItem, Long> {
    public List<PlaidItem> findAllByPlaidUser(FinanceUser financeUser);
}
