package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaidAccountRepository extends JpaRepository<PlaidAccount, Long> {
    public List<PlaidAccount> findAllByUser(FinanceUser financeUser);

    public PlaidAccount findByPlaidAccountId(String plaidAccountId);

}
