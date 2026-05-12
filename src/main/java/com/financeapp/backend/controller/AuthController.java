package com.financeapp.backend.controller;

import com.financeapp.backend.dto.AuthResponse;
import com.financeapp.backend.dto.LoginRequest;
import com.financeapp.backend.dto.RegisterRequest;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.security.JwtUtil;
import com.financeapp.backend.service.FinanceUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private FinanceUserService financeUserService;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    public AuthController(FinanceUserService financeUserService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.financeUserService = financeUserService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            FinanceUser financeUser = financeUserService.registerUser(
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName()
            );
            String token = jwtUtil.generateToken(financeUser);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtUtil.generateToken(financeUser);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch(Exception e){
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


}
