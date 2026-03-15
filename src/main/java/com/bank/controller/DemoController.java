package com.bank.controller;

import com.bank.singleton.BankManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping("/patterns")
    public ResponseEntity<Map<String, String>> getPatterns() {
        int totalTx = BankManager.getInstance().getTotalTransactions();
        Map<String, String> body = Map.of(
                "factoryPattern", "Created 3 account types via AccountFactory",
                "singletonPattern", "Total transactions: " + totalTx + " via BankManager.getInstance()",
                "observerPattern", "NotificationService auto-notifies on each transaction"
        );
        return ResponseEntity.ok(body);
    }
}
