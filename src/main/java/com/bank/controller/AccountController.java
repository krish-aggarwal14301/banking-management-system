package com.bank.controller;

import com.bank.dto.AccountDTO;
import com.bank.dto.AmountRequest;
import com.bank.dto.CreateAccountRequest;
import com.bank.dto.TransactionDTO;
import com.bank.dto.TransferRequest;
import com.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountDTO dto = accountService.createAccount(
                request.getCustomerId(),
                request.getAccountType(),
                request.getInitialDeposit()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{accNo}/deposit")
    public ResponseEntity<TransactionDTO> deposit(@PathVariable String accNo,
                                                 @Valid @RequestBody AmountRequest request) {
        TransactionDTO dto = accountService.deposit(accNo, request.getAmount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{accNo}/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@PathVariable String accNo,
                                                    @Valid @RequestBody AmountRequest request) {
        TransactionDTO dto = accountService.withdraw(accNo, request.getAmount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionDTO dto = accountService.transfer(
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{accNo}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String accNo) {
        double balance = accountService.getBalance(accNo);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<AccountDTO> list = accountService.getAccountsByCustomer(customerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{accNo}/interest")
    public ResponseEntity<Double> getInterest(@PathVariable String accNo) {
        double interest = accountService.calculateInterest(accNo);
        return ResponseEntity.ok(interest);
    }
}
