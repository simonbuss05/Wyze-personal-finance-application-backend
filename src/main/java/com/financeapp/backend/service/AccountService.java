package com.financeapp.backend.service;

import com.financeapp.backend.dto.AccountResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import com.financeapp.backend.repository.PlaidAccountRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            accountResponse.setNickname(plaidAccount.getNickname());
            accounts.add(accountResponse);
        }
        return accounts;
    }

    public PlaidAccount updateNickname (Long accountId, String nickname, FinanceUser financeUser) throws Exception {
        Optional<PlaidAccount> optionalPlaidAccount = plaidAccountRepository.findById(accountId);
        if (optionalPlaidAccount.isEmpty()) {
            throw new Exception("Account not found");
        }
        if (!optionalPlaidAccount.get().getUser().getId().equals(financeUser.getId())) {
            throw new Exception("Unauthorized");
        }
        optionalPlaidAccount.get().setNickname(nickname);
        plaidAccountRepository.save(optionalPlaidAccount.get());
        String nicknameToSave = (nickname == null || nickname.trim().isEmpty()) ? null : nickname.trim();
        optionalPlaidAccount.get().setNickname(nicknameToSave);
        return optionalPlaidAccount.get();
    }
}
