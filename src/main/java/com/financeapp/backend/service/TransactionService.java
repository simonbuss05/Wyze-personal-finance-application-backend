package com.financeapp.backend.service;

import com.financeapp.backend.dto.TransactionResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidTransaction;
import com.financeapp.backend.repository.PlaidAccountRepository;
import com.financeapp.backend.repository.PlaidTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransactionService {

    private PlaidTransactionRepository plaidTransactionRepository;
    private PlaidAccountRepository plaidAccountRepository;

    public TransactionService(PlaidTransactionRepository plaidTransactionRepository, PlaidAccountRepository plaidAccountRepository) {
        this.plaidTransactionRepository = plaidTransactionRepository;
        this.plaidAccountRepository = plaidAccountRepository;
    }

    public Page<TransactionResponse> getTransactionsForUser(FinanceUser financeUser, Long accountId, String category, String search, LocalDate from, LocalDate to, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<PlaidTransaction> plaidTransactions = plaidTransactionRepository
                .findTransactionsForUser(financeUser.getId(), accountId, category, search, from, to, pageRequest);

        return plaidTransactions.map(plaidTransaction -> {
            TransactionResponse response = new TransactionResponse();
            response.setId(plaidTransaction.getId());
            response.setPlaidTransactionId(plaidTransaction.getPlaidTransactionId());
            response.setAmount(plaidTransaction.getAmount());
            response.setMerchantName(plaidTransaction.getMerchantName());
            response.setName(plaidTransaction.getName());
            response.setCategory(plaidTransaction.getCategory());
            response.setDate(plaidTransaction.getDate());
            response.setPending(plaidTransaction.isPending());
            response.setAccountName(plaidTransaction.getPlaidAccount().getName());
            response.setAccountMask(plaidTransaction.getPlaidAccount().getMask());
            response.setInstitutionName(plaidTransaction.getPlaidAccount().getPlaidItem().getInstitutionName());
            return response;
        });
    }
}