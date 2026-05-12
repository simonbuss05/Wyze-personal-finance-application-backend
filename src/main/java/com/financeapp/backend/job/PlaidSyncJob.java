package com.financeapp.backend.job;


import com.financeapp.backend.entity.PlaidItem;
import com.financeapp.backend.repository.PlaidItemRepository;
import com.financeapp.backend.service.PlaidService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PlaidSyncJob {

    private PlaidService plaidService;
    private PlaidItemRepository plaidItemRepository;

    public PlaidSyncJob(PlaidService plaidService, PlaidItemRepository plaidItemRepository) {
        this.plaidService = plaidService;
        this.plaidItemRepository = plaidItemRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void syncAllTransactions() {
        List<PlaidItem> plaidItems = plaidItemRepository.findAll();
        for (PlaidItem plaidItem : plaidItems) {
            try{
                plaidService.syncTransactions(plaidItem);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
