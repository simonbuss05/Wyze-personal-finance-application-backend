package com.financeapp.backend.controller;

import com.financeapp.backend.dto.UpdatePasswordRequest;
import com.financeapp.backend.dto.UpdateProfileRequest;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.service.FinanceUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private FinanceUserService financeUserService;

    public UserController(FinanceUserService financeUserService) {
        this.financeUserService = financeUserService;
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) throws Exception {
        try{
            FinanceUser financeUser = getCurrentUser();
            financeUserService.updateProfile(financeUser, updateProfileRequest.getFirstName(), updateProfileRequest.getLastName(), updateProfileRequest.getEmail());
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);

        return financeUser;
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) throws Exception {
        try{
            FinanceUser financeUser = getCurrentUser();
            financeUserService.updatePassword(financeUser, updatePasswordRequest.getCurrentPassword(), updatePasswordRequest.getNewPassword());
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e){
            if (e.getMessage().equals("Current password is incorrect")){
                return ResponseEntity.status(401).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFinanceUser() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        try {
            financeUserService.deleteAccount(financeUser);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("firstName", financeUser.getFirstName());
        response.put("lastName", financeUser.getLastName());
        response.put("email", financeUser.getEmail());
        return ResponseEntity.ok(response);
    }
}
