package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaidAccountRepository extends JpaRepository<PlaidAccount, Long> {
    public List<PlaidAccount> findAllByUser(FinanceUser financeUser);

    public PlaidAccount findByPlaidAccountId(String plaidAccountId);

    FinanceUser user(FinanceUser user);

    @Query("SELECT COALESCE(SUM(a.currentBalance), 0) FROM PlaidAccount a WHERE a.user = :user AND a.type = 'depository'")
    Double sumDepositoryBalances(@Param("user") FinanceUser user);

    @Query("SELECT COALESCE(SUM(a.currentBalance), 0) FROM PlaidAccount a WHERE a.user = :user AND a.type = 'credit'")
    Double sumCreditBalances(@Param("user") FinanceUser user);

}
