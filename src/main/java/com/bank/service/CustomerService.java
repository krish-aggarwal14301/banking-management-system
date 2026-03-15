package com.bank.service;

import com.bank.dto.CustomerDTO;
import com.bank.dto.DTOMapper;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Customer;
import com.bank.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDTO register(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new InvalidAmountException("Email already registered");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(LocalDateTime.now());
        Customer saved = customerRepository.save(customer);
        return DTOMapper.toCustomerDTO(saved);
    }

    public Customer login(String email, String rawPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));
        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new AccountNotFoundException("Invalid credentials");
        }
        return customer;
    }

    public CustomerDTO getProfile(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));
        return DTOMapper.toCustomerDTO(customer);
    }

    public CustomerDTO getProfileByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));
        return DTOMapper.toCustomerDTO(customer);
    }

    public CustomerDTO updateProfile(Long id, Customer data) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));
        existing.setFullName(data.getFullName());
        existing.setPhone(data.getPhone());
        // Never update email or password here
        Customer saved = customerRepository.save(existing);
        return DTOMapper.toCustomerDTO(saved);
    }
}
