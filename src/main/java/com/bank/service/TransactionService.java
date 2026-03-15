package com.bank.service;

import com.bank.dto.DTOMapper;
import com.bank.dto.TransactionDTO;
import com.bank.exception.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getByAccountId(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId).stream()
                .map(DTOMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getByAccountIdFiltered(Long accountId, LocalDateTime from, LocalDateTime to) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }
        return transactionRepository.findByAccountIdAndTimestampBetween(accountId, from, to).stream()
                .map(DTOMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }
}
