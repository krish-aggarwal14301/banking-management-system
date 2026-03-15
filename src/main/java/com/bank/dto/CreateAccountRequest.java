package com.bank.dto;

import com.bank.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ✅ FIX #13 — @Valid validation
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private AccountType accountType;

    @Positive
    private double initialDeposit;
}
