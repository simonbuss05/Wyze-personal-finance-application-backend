package com.financeapp.backend.service;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidItem;
import com.financeapp.backend.repository.FinanceUserRepository;
import com.financeapp.backend.repository.PlaidAccountRepository;
import com.financeapp.backend.repository.PlaidItemRepository;
import com.financeapp.backend.repository.PlaidTransactionRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceUserService implements UserDetailsService {

    FinanceUserRepository financeUserRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private PlaidItemRepository plaidItemRepository;

    private PlaidAccountRepository plaidAccountRepository;

    private PlaidTransactionRepository plaidTransactionRepository;

    private PlaidService plaidService;

    public FinanceUserService(FinanceUserRepository financeUserRepository, PasswordEncoder passwordEncoder, PlaidItemRepository plaidItemRepository, PlaidAccountRepository plaidAccountRepository, PlaidTransactionRepository plaidTransactionRepository, PlaidService plaidService) {
        this.financeUserRepository = financeUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.plaidItemRepository = plaidItemRepository;
        this.plaidAccountRepository = plaidAccountRepository;
        this.plaidTransactionRepository = plaidTransactionRepository;
        this.plaidService = plaidService;
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

    public FinanceUser updateProfile(FinanceUser financeUser, String firstName, String lastName, String email) throws Exception {
        boolean exists = financeUserRepository.existsByEmail(email);
        if (exists && !email.equals(financeUser.getEmail())) {
            throw new Exception("Email already in use");
        }
        financeUser.setFirstName(firstName);
        financeUser.setLastName(lastName);
        financeUser.setEmail(email);
        financeUserRepository.save(financeUser);
        return financeUser;
    }

    public FinanceUser updatePassword (FinanceUser financeUser, String currentPassword, String newPassword) throws Exception {
        boolean matches = passwordEncoder.matches(currentPassword, financeUser.getPassword());
        if (!matches) {
            throw new Exception("Current password is incorrect");
        }
        String hashedPassword = passwordEncoder.encode(newPassword);
        financeUser.setPassword(hashedPassword);
        financeUserRepository.save(financeUser);
        return financeUser;
    }

    public void deleteAccount(FinanceUser financeUser) throws Exception {
        List<PlaidItem> plaidItemList = plaidItemRepository.findAllByPlaidUser(financeUser);
        for (PlaidItem plaidItem : plaidItemList) {
            plaidService.removeItem(plaidItem);
        }
        plaidTransactionRepository.deleteAll(plaidTransactionRepository.findAllByPlaidUser(financeUser));
        plaidAccountRepository.deleteAll(plaidAccountRepository.findAllByUser(financeUser));
        plaidItemRepository.deleteAll(plaidItemList);
        financeUserRepository.delete(financeUser);
    }


}