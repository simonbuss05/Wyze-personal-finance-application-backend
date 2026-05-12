package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaidTransactionRepository extends JpaRepository<PlaidTransaction, Long> {
    public List<PlaidTransaction> findAllByPlaidUser (FinanceUser financeUser);

    public PlaidTransaction findByPlaidTransactionId(String plaidTransactionId);

}
