package com.bank.controller;

import com.bank.dto.TransactionDTO;
import com.bank.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getByAccount(@PathVariable Long accountId) {
        List<TransactionDTO> list = transactionService.getByAccountId(accountId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{accountId}/filter")
    public ResponseEntity<List<TransactionDTO>> getByAccountFiltered(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<TransactionDTO> list = transactionService.getByAccountIdFiltered(accountId, from, to);
        return ResponseEntity.ok(list);
    }
}
