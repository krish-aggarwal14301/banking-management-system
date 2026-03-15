package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String accountNumber;
    private double balance;
    private String accountType;
    private String status;
    private LocalDateTime createdAt;
    private String ownerName;
    // Only for FD accounts
    private Double maturityInterest;
}
