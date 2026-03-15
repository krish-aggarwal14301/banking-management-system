package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {

    private long totalCustomers;
    private long totalAccounts;
    private double totalBalance;
    private long totalTransactionsAllTime;  // accurate DB count — Fix #12
    private int transactionsThisSession;    // Singleton demo — Fix #12
}
