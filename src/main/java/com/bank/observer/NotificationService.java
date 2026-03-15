package com.bank.observer;

import org.springframework.stereotype.Service;

@Service
public class NotificationService implements TransactionObserver {

    // OBSERVER PATTERN: automatically notified on every transaction
    @Override
    public void onTransaction(String accountNumber, String type, double amount) {
        System.out.println("NOTIFICATION: " + type + " of ₹" + amount + " on account " + accountNumber);
    }
}
