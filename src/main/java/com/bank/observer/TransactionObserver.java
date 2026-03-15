package com.bank.observer;

public interface TransactionObserver {

    void onTransaction(String accountNumber, String type, double amount);
}
