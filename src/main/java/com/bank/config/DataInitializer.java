package com.bank.config;

import com.bank.enums.AccountType;
import com.bank.factory.AccountFactory;
import com.bank.model.*;
import com.bank.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (customerRepository.count() > 0) return; // already initialized

        // Customer 1
        Customer alice = new Customer();
        alice.setFullName("Alice sharma");
        alice.setEmail("alice@bank.com");
        alice.setPhone("9876543210");
        alice.setPassword(passwordEncoder.encode("password123"));
        customerRepository.save(alice);

        // Customer 2
        Customer bob = new Customer();
        bob.setFullName("Bob Verma");
        bob.setEmail("bob@bank.com");
        bob.setPhone("9876543211");
        bob.setPassword(passwordEncoder.encode("password123"));
        customerRepository.save(bob);

        // Customer 3
        Customer charlie = new Customer();
        charlie.setFullName("Charlie Singh");
        charlie.setEmail("charlie@bank.com");
        charlie.setPhone("9876543212");
        charlie.setPassword(passwordEncoder.encode("password123"));
        customerRepository.save(charlie);

        // Accounts
        Account aliceSavings = AccountFactory.createAccount(
            AccountType.SAVINGS, "ACC202500001", 50000, alice);
        accountRepository.save(aliceSavings);

        Account aliceFD = AccountFactory.createAccount(
            AccountType.FIXED_DEPOSIT, "ACC202500002", 100000, alice);
        accountRepository.save(aliceFD);

        Account bobCurrent = AccountFactory.createAccount(
            AccountType.CURRENT, "ACC202500003", 75000, bob);
        accountRepository.save(bobCurrent);

        Account bobSavings = AccountFactory.createAccount(
            AccountType.SAVINGS, "ACC202500004", 30000, bob);
        accountRepository.save(bobSavings);

        Account charlieSavings = AccountFactory.createAccount(
            AccountType.SAVINGS, "ACC202500005", 25000, charlie);
        accountRepository.save(charlieSavings);

        System.out.println("✅ Sample data loaded!");
        System.out.println("Login with: alice@bank.com / password123");
        System.out.println("Login with: bob@bank.com / password123");
        System.out.println("Login with: charlie@bank.com / password123");
    }
}