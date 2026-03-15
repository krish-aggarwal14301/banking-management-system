package com.bank.model;

import com.bank.enums.AccountStatus;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidAmountException;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends Account {

    private double overdraftLimit = 10000;

    public CurrentAccount() {
    }

    public CurrentAccount(String accountNumber, double initialDeposit, Customer owner) {
        super(accountNumber, initialDeposit, owner);
    }

    @Override
    public double calculateInterest() {
        return 0;
    }

    // ✅ FIX #14: Do NOT call super.withdraw() — different balance logic
    // ✅ FIX #24: BUT manually include parent safety checks (skipping super doesn't mean skipping validation!)
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        if (getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Account is frozen");
        }
        if (getBalance() + overdraftLimit < amount) {
            throw new InsufficientBalanceException("Overdraft limit exceeded");
        }
        setBalance(getBalance() - amount);
    }
}
