package com.bank.dto;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.CurrentAccount;
import com.bank.model.FixedDepositAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.Transaction;

public class DTOMapper {

    public static CustomerDTO toCustomerDTO(Customer c) {
        if (c == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(c.getId());
        dto.setFullName(c.getFullName());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        return dto;
    }

    public static AccountDTO toAccountDTO(Account a) {
        if (a == null) return null;
        AccountDTO dto = new AccountDTO();
        dto.setId(a.getId());
        dto.setAccountNumber(a.getAccountNumber());
        dto.setBalance(a.getBalance());
        dto.setAccountType(getAccountTypeString(a));
        dto.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
        dto.setCreatedAt(a.getCreatedAt());
        dto.setOwnerName(a.getOwner() != null ? a.getOwner().getFullName() : null);
        dto.setMaturityInterest(a instanceof FixedDepositAccount
                ? ((FixedDepositAccount) a).calculateInterest()
                : null);
        return dto;
    }

    private static String getAccountTypeString(Account a) {
        if (a instanceof SavingsAccount) return "SAVINGS";
        if (a instanceof CurrentAccount) return "CURRENT";
        if (a instanceof FixedDepositAccount) return "FIXED_DEPOSIT";
        return a.getClass().getSimpleName();
    }

    public static TransactionDTO toTransactionDTO(Transaction t) {
        if (t == null) return null;
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setTransactionType(t.getTransactionType() != null ? t.getTransactionType().name() : null);
        dto.setAmount(t.getAmount());
        dto.setTimestamp(t.getTimestamp());
        dto.setDescription(t.getDescription());
        return dto;
    }
}
