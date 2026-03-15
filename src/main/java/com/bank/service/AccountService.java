package com.bank.service;

import com.bank.dto.AccountDTO;
import com.bank.dto.DTOMapper;
import com.bank.dto.TransactionDTO;
import com.bank.enums.AccountStatus;
import com.bank.enums.AccountType;
import com.bank.enums.TransactionType;
import com.bank.exception.AccountNotFoundException;
import com.bank.factory.AccountFactory;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.observer.NotificationService;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import com.bank.singleton.BankManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository,
                          TransactionRepository transactionRepository,
                          NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AccountDTO createAccount(Long customerId, AccountType type, double initialDeposit) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));
        String accountNumber = "ACC" + LocalDate.now().getYear()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        Account account = AccountFactory.createAccount(type, accountNumber, initialDeposit, customer);
        Account saved = accountRepository.save(account);
        return DTOMapper.toAccountDTO(saved);
    }

    @Transactional
    public TransactionDTO deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        account.deposit(amount, "Deposit via banking app");
        accountRepository.save(account);
        Transaction tx = new Transaction(TransactionType.DEPOSIT, amount, "Deposit via banking app", account);
        transactionRepository.save(tx);
        notificationService.onTransaction(accountNumber, "DEPOSIT", amount);
        BankManager.getInstance().incrementTransactions();
        return DTOMapper.toTransactionDTO(tx);
    }

    @Transactional
    public TransactionDTO withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        account.withdraw(amount, "Withdrawal via banking app");
        accountRepository.save(account);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal via banking app", account);
        transactionRepository.save(tx);
        notificationService.onTransaction(accountNumber, "WITHDRAWAL", amount);
        BankManager.getInstance().incrementTransactions();
        return DTOMapper.toTransactionDTO(tx);
    }

    @Transactional
    public TransactionDTO transfer(String fromAccNo, String toAccNo, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccNo)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + fromAccNo));
        Account toAccount = accountRepository.findByAccountNumber(toAccNo)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + toAccNo));
        fromAccount.transfer(amount, toAccount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction txOut = new Transaction(TransactionType.TRANSFER, amount, "Transfer to " + toAccNo, fromAccount);
        Transaction txIn = new Transaction(TransactionType.TRANSFER, amount, "Transfer from " + fromAccNo, toAccount);
        transactionRepository.save(txOut);
        transactionRepository.save(txIn);
        notificationService.onTransaction(fromAccNo, "TRANSFER", amount);
        notificationService.onTransaction(toAccNo, "TRANSFER", amount);
        BankManager.getInstance().incrementTransactions();
        return DTOMapper.toTransactionDTO(txOut);
    }

    @Transactional(readOnly = true)
    public double getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByCustomer(Long customerId) {
        List<Account> accounts = accountRepository.findByOwnerId(customerId);
        return accounts.stream().map(DTOMapper::toAccountDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public double calculateInterest(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        return account.calculateInterest();
    }

    @Transactional
    public AccountDTO freezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        account.setStatus(AccountStatus.FROZEN);
        Account saved = accountRepository.save(account);
        return DTOMapper.toAccountDTO(saved);
    }

    @Transactional
    public AccountDTO unfreezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        account.setStatus(AccountStatus.ACTIVE);
        Account saved = accountRepository.save(account);
        return DTOMapper.toAccountDTO(saved);
    }
}
