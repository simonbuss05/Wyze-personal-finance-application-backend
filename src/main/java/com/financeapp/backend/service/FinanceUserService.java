package com.financeapp.backend.service;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.repository.FinanceUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FinanceUserService implements UserDetailsService {

    FinanceUserRepository financeUserRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public FinanceUserService(FinanceUserRepository financeUserRepository, PasswordEncoder passwordEncoder) {
        this.financeUserRepository = financeUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

   public FinanceUser registerUser(String email, String password, String firstName, String lastName) throws Exception {
        // checks if email already exists; throws exception if it does
        if (financeUserRepository.existsByEmail(email)) {
            throw new Exception("Email already exists");
        }
        // hashes password
        String hashedPassword = passwordEncoder.encode(password);

        // creates new FinanceUser object with given parameters
        FinanceUser financeUser = new FinanceUser(email, hashedPassword, firstName, lastName);
        // saves object into database
        financeUserRepository.save(financeUser);
        // returns object
        return financeUser;
   }

   public FinanceUser loadUserByEmail(String email) throws Exception {
        // checks to make sure email exists
        if (!financeUserRepository.existsByEmail(email)) {
            throw new Exception("Email does not exist");
        }
        // returns user object of certain email
       return financeUserRepository.findByEmail(email);
   }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!financeUserRepository.existsByEmail(username)) {
            throw new UsernameNotFoundException(username);
        }
        return financeUserRepository.findByEmail(username);
    }
}
