package com.bank.interfaces;

import com.bank.model.Account;

public interface Transactable {

    void deposit(double amount);

    // ✅ FIX #16: OVERLOADED
    void deposit(double amount, String description);

    void withdraw(double amount);

    // ✅ FIX #16: OVERLOADED
    void withdraw(double amount, String reason);

    void transfer(double amount, Account target);
}
