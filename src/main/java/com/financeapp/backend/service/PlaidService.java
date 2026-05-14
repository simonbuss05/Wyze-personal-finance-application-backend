package com.financeapp.backend.service;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import com.financeapp.backend.entity.PlaidItem;
import com.financeapp.backend.entity.PlaidTransaction;
import com.financeapp.backend.repository.PlaidAccountRepository;
import com.financeapp.backend.repository.PlaidItemRepository;
import com.financeapp.backend.repository.PlaidTransactionRepository;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidService {

    private final PlaidTransactionRepository plaidTransactionRepository;
    private PlaidApi plaidApi;
    private PlaidItemRepository plaidItemRepository;
    private PlaidAccountRepository plaidAccountRepository;

    public PlaidService(PlaidApi plaidApi, PlaidItemRepository plaidItemRepository, PlaidAccountRepository plaidAccountRepository, PlaidTransactionRepository plaidTransactionRepository) {
        this.plaidApi = plaidApi;
        this.plaidItemRepository = plaidItemRepository;
        this.plaidAccountRepository = plaidAccountRepository;
        this.plaidTransactionRepository = plaidTransactionRepository;
    }

    public String createLinkToken(FinanceUser financeUser) throws IOException {
        LinkTokenCreateRequestUser linkUser = new LinkTokenCreateRequestUser()
            .clientUserId(String.valueOf(financeUser.getId()));

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .clientName("Wyze")
                .language("en")
                .countryCodes(List.of(CountryCode.US))
                .user(linkUser)
                .products(List.of(Products.TRANSACTIONS));
        Response<LinkTokenCreateResponse> response = plaidApi.linkTokenCreate(request).execute();

        if (response.body() == null) {
            throw new IOException("Failed to create link token");
        }

        return response.body().getLinkToken();

    }

    public PlaidItem exchangeToken(String publicToken, FinanceUser financeUser) throws IOException {
        // Step 1 — Exchange public token for access token
        ItemPublicTokenExchangeRequest exchangeRequest = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        Response<ItemPublicTokenExchangeResponse> exchangeResponse = plaidApi
                .itemPublicTokenExchange(exchangeRequest)
                .execute();

        if (exchangeResponse.body() == null) {
            throw new IOException("Failed to exchange public token");
        }

        String accessToken = exchangeResponse.body().getAccessToken();
        String itemId = exchangeResponse.body().getItemId();

        // Step 2 — Get institution ID from item
        ItemGetRequest itemGetRequest = new ItemGetRequest()
                .accessToken(accessToken);

        Response<ItemGetResponse> itemGetResponse = plaidApi
                .itemGet(itemGetRequest)
                .execute();

        if (itemGetResponse.body() == null) {
            throw new IOException("Failed to get item");
        }

        String institutionId = itemGetResponse.body().getItem().getInstitutionId();

        // Step 3 — Get institution name from institution ID
        InstitutionsGetByIdRequest institutionRequest = new InstitutionsGetByIdRequest()
                .institutionId(institutionId)
                .countryCodes(List.of(CountryCode.US));

        Response<InstitutionsGetByIdResponse> institutionResponse = plaidApi
                .institutionsGetById(institutionRequest)
                .execute();

        if (institutionResponse.body() == null) {
            throw new IOException("Failed to get institution");
        }

        String institutionName = institutionResponse.body().getInstitution().getName();

        // Step 4 — Save PlaidItem
        PlaidItem plaidItem = new PlaidItem();
        plaidItem.setAccessToken(accessToken);
        plaidItem.setItemId(itemId);
        plaidItem.setInstitutionId(institutionId);
        plaidItem.setInstitutionName(institutionName);
        plaidItem.setPlaidUser(financeUser);

        plaidItemRepository.save(plaidItem);
        return plaidItem;
    }

    public void syncAccounts(PlaidItem plaidItem) throws IOException {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken(plaidItem.getAccessToken());

        Response<AccountsGetResponse> accountsGetResponse = plaidApi.accountsGet(accountsGetRequest).execute();
        if (accountsGetResponse.body() == null) {
            throw new IOException("Failed to get accounts");
        }

        List<AccountBase> accounts = accountsGetResponse.body().getAccounts();

        for (AccountBase account : accounts) {
            PlaidAccount existing = plaidAccountRepository.findByPlaidAccountId(account.getAccountId());
            if (existing != null) {
                existing.setCurrentBalance(account.getBalances().getCurrent());
                existing.setAvailableBalance(account.getBalances().getAvailable());
                plaidAccountRepository.save(existing);
            } else {
                PlaidAccount plaidAccount = getPlaidAccount(plaidItem, account);
                plaidAccountRepository.save(plaidAccount);
            }
        }
    }

    private static PlaidAccount getPlaidAccount(PlaidItem plaidItem, AccountBase account) {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setPlaidAccountId(account.getAccountId());
        plaidAccount.setName(account.getName());
        plaidAccount.setOfficialName(account.getOfficialName());
        plaidAccount.setType(account.getType().getValue());
        assert account.getSubtype() != null;
        plaidAccount.setSubtype(account.getSubtype().getValue());
        plaidAccount.setMask(account.getMask());
        plaidAccount.setCurrentBalance(account.getBalances().getCurrent());
        plaidAccount.setAvailableBalance(account.getBalances().getAvailable());
        plaidAccount.setCurrencyCode(account.getBalances().getIsoCurrencyCode());
        plaidAccount.setPlaidItem(plaidItem);
        plaidAccount.setUser(plaidItem.getPlaidUser());
        return plaidAccount;
    }

    public void syncTransactions(PlaidItem plaidItem) throws IOException {
        boolean hasMore = true;
        String cursor = plaidItem.getCursor();

        while (hasMore) {
            TransactionsSyncRequest transactionsSyncRequest = new TransactionsSyncRequest()
                    .accessToken(plaidItem.getAccessToken());
            if (cursor != null) {
                transactionsSyncRequest.cursor(cursor);
            }
            Response<TransactionsSyncResponse> transactionsSyncResponse =  plaidApi.transactionsSync(transactionsSyncRequest).execute();
            if (transactionsSyncResponse.body() == null) {
                throw new IOException("Failed to get transactions");
            }
            List<Transaction> transactions = transactionsSyncResponse.body().getAdded();
            for (Transaction transaction : transactions) {
                if (plaidTransactionRepository.findByPlaidTransactionId(transaction.getTransactionId()) == null) {
                    PlaidTransaction plaidTransaction = new PlaidTransaction();
                    plaidTransaction.setPlaidTransactionId(transaction.getTransactionId());
                    plaidTransaction.setAmount(transaction.getAmount());
                    plaidTransaction.setMerchantName(transaction.getMerchantName());
                    plaidTransaction.setName(transaction.getName());
                    if (transaction.getPersonalFinanceCategory() != null) {
                        plaidTransaction.setCategory(transaction.getPersonalFinanceCategory().getPrimary());
                    } else if (transaction.getCategory() != null && !transaction.getCategory().isEmpty()) {
                        plaidTransaction.setCategory(transaction.getCategory().get(0));
                    }
                    plaidTransaction.setDate(transaction.getDate());
                    plaidTransaction.setPending(transaction.getPending());
                    plaidTransaction.setPlaidAccount(plaidAccountRepository.findByPlaidAccountId(transaction.getAccountId()));
                    plaidTransaction.setPlaidUser(plaidItem.getPlaidUser());
                    plaidTransactionRepository.save(plaidTransaction);

                }
            }
            cursor = transactionsSyncResponse.body().getNextCursor();
            hasMore = transactionsSyncResponse.body().getHasMore();


        }
        plaidItem.setCursor(cursor);
        plaidItemRepository.save(plaidItem);

    }


    public void removeItem(PlaidItem plaidItem) throws Exception {
        try {
            ItemRemoveRequest removeItemRequest = new ItemRemoveRequest();
            removeItemRequest.accessToken(plaidItem.getAccessToken());
            plaidApi.itemRemove(removeItemRequest).execute();
        } catch (Exception e) {
            System.out.println("Failed to remove item");
        }

    }

    public void disconnectPlaidItem(Long itemId, FinanceUser financeUser) throws Exception {
        PlaidItem plaidItem = plaidItemRepository.findById(itemId).orElse(null);
        if (plaidItem == null) {
            throw new Exception("Item not found");
        }
        if (!plaidItem.getPlaidUser().getId().equals(financeUser.getId())) {
            throw new Exception("Unauthorized");
        }
        removeItem(plaidItem);

        List<PlaidAccount> plaidAccountsList = plaidAccountRepository.findAllByPlaidItem(plaidItem);
        plaidTransactionRepository.deleteAllByPlaidAccountIn(plaidAccountsList);
        plaidAccountRepository.deleteAll(plaidAccountsList);
        plaidItemRepository.delete(plaidItem);

    }

















}
