package com.bank.model;

import com.bank.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SAVINGS")
@NoArgsConstructor
public class SavingsAccount extends Account {

    private static final double MIN_BALANCE = 1000;
    public static final double DAILY_WITHDRAWAL_LIMIT = 50000;

    private double interestRate = 4.5;

    public SavingsAccount(String accountNumber, double initialDeposit, Customer owner) {
        super(accountNumber, initialDeposit, owner);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * interestRate / 100;
    }

    @Override
    public void withdraw(double amount) {
        if (getBalance() - amount < MIN_BALANCE) {
            throw new InsufficientBalanceException("Minimum balance of " + MIN_BALANCE + " must be maintained");
        }
        super.withdraw(amount);
    }
}
