package com.bank.factory;

import com.bank.enums.AccountType;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.Customer;
import com.bank.model.FixedDepositAccount;
import com.bank.model.SavingsAccount;

// FACTORY PATTERN: encapsulates object creation logic
public class AccountFactory {

    public static Account createAccount(
            AccountType type, String accountNumber,
            double initialDeposit, Customer owner) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, initialDeposit, owner);
            case CURRENT:
                return new CurrentAccount(accountNumber, initialDeposit, owner);
            case FIXED_DEPOSIT:
                return new FixedDepositAccount(accountNumber, initialDeposit, owner);
            default:
                throw new InvalidAmountException("Unknown account type");
        }
    }
}
