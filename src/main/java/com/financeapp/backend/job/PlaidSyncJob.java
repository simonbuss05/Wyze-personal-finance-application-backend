package com.financeapp.backend.job;


import com.financeapp.backend.entity.PlaidItem;
import com.financeapp.backend.repository.PlaidItemRepository;
import com.financeapp.backend.service.PlaidService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaidSyncJob {

    private PlaidService plaidService;
    private PlaidItemRepository plaidItemRepository;

    public PlaidSyncJob(PlaidService plaidService, PlaidItemRepository plaidItemRepository) {
        this.plaidService = plaidService;
        this.plaidItemRepository = plaidItemRepository;
    }

    @Scheduled(fixedRate = 60000)
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
