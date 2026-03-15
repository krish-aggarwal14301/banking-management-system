package com.bank.model;

import com.bank.exception.FDMaturityException;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("FIXED_DEPOSIT")
public class FixedDepositAccount extends Account {

    private static final double FIXED_INTEREST_RATE = 7.0;

    private LocalDateTime maturityDate;

    public FixedDepositAccount() {
        super();
        this.maturityDate = LocalDateTime.now().plusMonths(6);
    }

    public FixedDepositAccount(String accountNumber, double initialDeposit, Customer owner) {
        super(accountNumber, initialDeposit, owner);
        this.maturityDate = LocalDateTime.now().plusMonths(6);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * FIXED_INTEREST_RATE / 100;
    }

    @Override
    public void withdraw(double amount) {
        if (LocalDateTime.now().isBefore(maturityDate)) {
            throw new FDMaturityException("Fixed deposit withdrawal not allowed before maturity date");
        }
        super.withdraw(amount);
    }
}
