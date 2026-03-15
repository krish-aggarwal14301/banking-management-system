package com.bank.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ✅ FIX #13 — @Valid validation
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountRequest {

    @Positive
    private double amount;
}
