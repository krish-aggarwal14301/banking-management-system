package com.bank.service;

import com.bank.dto.AccountDTO;
import com.bank.dto.CustomerDTO;
import com.bank.dto.DTOMapper;
import com.bank.dto.StatsDTO;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import com.bank.singleton.BankManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AdminService(CustomerRepository customerRepository,
                        AccountRepository accountRepository,
                        TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(DTOMapper::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(DTOMapper::toAccountDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public double getTotalBankBalance() {
        return accountRepository.findAll().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Transactional(readOnly = true)
    public StatsDTO getStats() {
        return new StatsDTO(
                customerRepository.count(),
                accountRepository.count(),
                getTotalBankBalance(),
                transactionRepository.count(),           // accurate DB count — Fix #12
                BankManager.getInstance().getTotalTransactions()  // Singleton demo — Fix #12
        );
    }
}
