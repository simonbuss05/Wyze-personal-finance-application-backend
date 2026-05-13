package com.financeapp.backend.service;

import com.financeapp.backend.dto.AccountResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import com.financeapp.backend.repository.PlaidAccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private PlaidAccountRepository plaidAccountRepository;
    private FinanceUserService financeUserService;

    public AccountService(PlaidAccountRepository plaidAccountRepository, FinanceUserService financeUserService) {
        this.plaidAccountRepository = plaidAccountRepository;
        this.financeUserService = financeUserService;
    }

    public List<AccountResponse> getAccountsForUser(FinanceUser financeUser) {
        List<PlaidAccount> plaidAccounts = plaidAccountRepository.findAllByUser(financeUser);
        List<AccountResponse> accounts = new ArrayList<>();
        for (PlaidAccount plaidAccount : plaidAccounts) {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setId(plaidAccount.getId());
            accountResponse.setPlaidAccountId(plaidAccount.getPlaidAccountId());
            accountResponse.setName(plaidAccount.getName());
            accountResponse.setOfficialName(plaidAccount.getOfficialName());
            accountResponse.setType(plaidAccount.getType());
            accountResponse.setSubtype(plaidAccount.getSubtype());
            accountResponse.setMask(plaidAccount.getMask());
            accountResponse.setCurrentBalance(plaidAccount.getCurrentBalance());
            accountResponse.setAvailableBalance(plaidAccount.getAvailableBalance());
            accountResponse.setCurrencyCode(plaidAccount.getCurrencyCode());
            accountResponse.setInstitutionName(plaidAccount.getPlaidItem().getInstitutionName());
            accounts.add(accountResponse);
        }
        return accounts;
    }
}
