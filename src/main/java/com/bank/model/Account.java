package com.bank.model;

import com.bank.enums.AccountStatus;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidAmountException;
import com.bank.interfaces.Transactable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public abstract class Account implements Transactable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private double balance;  // PRIVATE — encapsulation

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    // ✅ FIX #2: No FetchType.LAZY — default EAGER loads owner immediately
    // Prevents LazyInitializationException in DTOMapper
    @JoinColumn(name = "customer_id")
    // ✅ FIX #1: @JsonBackReference prevents JSON infinite loop
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Customer owner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    // ✅ FIX #1: @JsonManagedReference — parent side
    @JsonManagedReference
    private List<Transaction> transactions;

    // Default constructor
    public Account() {
        this.createdAt = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
    }

    // Parameterized constructor
    public Account(String accountNumber, double initialDeposit, Customer owner) {
        this();
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.owner = owner;
    }

    // ABSTRACT — forces every child to define its own interest rate
    public abstract double calculateInterest();

    // ENCAPSULATION — controlled deposit
    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        if (this.status == AccountStatus.FROZEN)
            throw new AccountFrozenException("Account " + accountNumber + " is frozen");
        this.balance += amount;
    }

    // ENCAPSULATION — controlled withdrawal
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        if (this.status == AccountStatus.FROZEN)
            throw new AccountFrozenException("Account " + accountNumber + " is frozen");
        if (this.balance < amount)
            throw new InsufficientBalanceException("Insufficient balance. Available: " + balance);
        this.balance -= amount;
    }

    @Override
    public void transfer(double amount, Account target) {
        this.withdraw(amount);
        target.deposit(amount);
    }

    // Getters and Setters — written manually for viva
    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }

    // ✅ FIX #14: PROTECTED — only child classes can use this
    // private = only Account, protected = Account + children, public = everyone
    // This shows 3 levels of access modifiers in viva!
    protected void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Customer getOwner() { return owner; }
    public void setOwner(Customer owner) { this.owner = owner; }
    public List<Transaction> getTransactions() { return transactions; }

    @Override
    public String toString() {
        return "Account{id=" + id + ", accountNumber='" + accountNumber +
               "', balance=" + balance + ", status=" + status + "}";
    }

    // ✅ FIX #16: METHOD OVERLOADING — same name, different parameters
    // deposit(amount) = Version 1
    // deposit(amount, description) = Version 2 — OVERLOADED
    public void deposit(double amount, String description) {
        deposit(amount);  // calls Version 1
        System.out.println("Deposit note: " + description);
    }

    // withdraw(amount) = Version 1 (already defined above)
    // withdraw(amount, reason) = Version 2 — OVERLOADED
    public void withdraw(double amount, String reason) {
        withdraw(amount);  // calls Version 1
        System.out.println("Withdrawal reason: " + reason);
    }
    // Viva: "deposit(amount) and deposit(amount, description) have same name
    // but different parameters — compile-time polymorphism / overloading"
}