package com.financeapp.backend.controller;

import com.financeapp.backend.service.PlaidService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/plaid")
public class PlaidController {

    private PlaidService plaidService;

    public PlaidController(PlaidService plaidService) {
        this.plaidService = plaidService;
    }




}
