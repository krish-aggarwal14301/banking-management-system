package com.bank.singleton;

// SINGLETON PATTERN: only one BankManager instance exists
// NOTE: Spring @Service beans are also singletons — this demonstrates
// the MANUAL Singleton pattern for OOP course purposes.
public class BankManager {

    private static BankManager instance;
    private static int totalTransactions = 0;

    private BankManager() {
    }

    // ✅ FIX #19: synchronized for thread-safety
    public static synchronized BankManager getInstance() {
        if (instance == null) {
            instance = new BankManager();
        }
        return instance;
    }

    public void incrementTransactions() {
        totalTransactions++;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }
}
