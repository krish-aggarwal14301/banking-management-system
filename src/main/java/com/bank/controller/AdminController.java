package com.bank.controller;

import com.bank.dto.AccountDTO;
import com.bank.dto.CustomerDTO;
import com.bank.dto.StatsDTO;
import com.bank.service.AdminService;
import com.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AccountService accountService;

    public AdminController(AdminService adminService, AccountService accountService) {
        this.adminService = adminService;
        this.accountService = accountService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @PutMapping("/accounts/{accNo}/freeze")
    public ResponseEntity<String> freezeAccount(@PathVariable String accNo) {
        accountService.freezeAccount(accNo);
        return ResponseEntity.ok("Account frozen");
    }

    @PutMapping("/accounts/{accNo}/unfreeze")
    public ResponseEntity<String> unfreezeAccount(@PathVariable String accNo) {
        accountService.unfreezeAccount(accNo);
        return ResponseEntity.ok("Account unfrozen");
    }
}
