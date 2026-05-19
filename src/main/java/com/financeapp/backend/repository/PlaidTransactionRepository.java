package com.financeapp.backend.repository;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import com.financeapp.backend.entity.PlaidTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlaidTransactionRepository extends JpaRepository<PlaidTransaction, Long> {
    public List<PlaidTransaction> findAllByPlaidUser (FinanceUser financeUser);

    public PlaidTransaction findByPlaidTransactionId(String plaidTransactionId);

    @Query(value = "SELECT * FROM plaid_transactions pt WHERE pt.user_id = :userId " +
            "AND (:accountId IS NULL OR pt.account_id = :accountId) " +
            "AND (:category IS NULL OR pt.category = :category) " +
            "AND (:search IS NULL OR pt.merchant_name ILIKE CONCAT('%', :search, '%') " +
            "OR pt.name ILIKE CONCAT('%', :search, '%')) " +
            "AND (:from IS NULL OR pt.date >= CAST(:from AS DATE)) " +
            "AND (:to IS NULL OR pt.date <= CAST(:to AS DATE)) " +
            "ORDER BY pt.date DESC",
            countQuery = "SELECT COUNT(*) FROM plaid_transactions pt WHERE pt.user_id = :userId " +
                    "AND (:accountId IS NULL OR pt.account_id = :accountId) " +
                    "AND (:category IS NULL OR pt.category = :category) " +
                    "AND (:search IS NULL OR pt.merchant_name ILIKE CONCAT('%', :search, '%') " +
                    "OR pt.name ILIKE CONCAT('%', :search, '%')) " +
                    "AND (:from IS NULL OR pt.date >= CAST(:from AS DATE)) " +
                    "AND (:to IS NULL OR pt.date <= CAST(:to AS DATE))",
            nativeQuery = true)
    Page<PlaidTransaction> findTransactionsForUser(
            @Param("userId") Long userId,
            @Param("accountId") Long accountId,
            @Param("category") String category,
            @Param("search") String search,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM PlaidTransaction t WHERE t.plaidUser = :user AND t.amount > 0 AND MONTH(t.date) = MONTH(CURRENT_DATE) AND YEAR(t.date) = YEAR(CURRENT_DATE)")
    Double sumMonthlySpending(@Param("user") FinanceUser user);


    @Query("SELECT DISTINCT t.category FROM PlaidTransaction t WHERE t.plaidUser = :user AND t.category IS NOT NULL ORDER BY t.category")
    List<String> findDistinctCategoriesByUser(@Param("user") FinanceUser user);

    void deleteAllByPlaidAccountIn(List<PlaidAccount> accounts);

    @Query(value = "SELECT TO_CHAR(DATE_TRUNC('month', t.date), 'Mon YYYY') as month, " +
            "COALESCE(SUM(t.amount), 0) as total " +
            "FROM plaid_transactions t " +
            "WHERE t.user_id = :userId " +
            "AND t.amount > 0 " +
            "AND t.date >= :startDate " +
            "GROUP BY DATE_TRUNC('month', t.date) " +
            "ORDER BY DATE_TRUNC('month', t.date) ASC",
            nativeQuery = true)
    List<Object[]> getMonthlySpending(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);

    @Query(value = "SELECT t.category, COALESCE(SUM(t.amount), 0) as total " +
            "FROM plaid_transactions t " +
            "WHERE t.user_id = :userId " +
            "AND t.amount > 0 " +
            "AND t.category IS NOT NULL " +
            "AND DATE_TRUNC('month', t.date) = DATE_TRUNC('month', CURRENT_DATE) " +
            "GROUP BY t.category " +
            "ORDER BY total DESC",
            nativeQuery = true)
    List<Object[]> getCategoryBreakdown(@Param("userId") Long userId);

    @Query(value = "SELECT t.merchant_name, t.name, t.amount FROM plaid_transactions t " +
            "WHERE t.user_id = :userId AND t.amount > 0 " +
            "AND DATE_TRUNC('month', t.date) = DATE_TRUNC('month', CURRENT_DATE) " +
            "ORDER BY t.amount DESC LIMIT 1", nativeQuery = true)
    Object[] getBiggestTransactionThisMonth(@Param("userId") Long userId);

    @Query(value = "SELECT COALESCE(SUM(t.amount), 0) FROM plaid_transactions t " +
            "WHERE t.user_id = :userId AND t.amount > 0 " +
            "AND t.date >= :startDate AND t.date <= :endDate",
            nativeQuery = true)
    Double sumSpendingBetweenDates(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
