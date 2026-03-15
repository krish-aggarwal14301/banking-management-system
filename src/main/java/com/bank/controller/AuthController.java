package com.bank.controller;

import com.bank.config.JwtUtil;
import com.bank.dto.CustomerDTO;
import com.bank.dto.LoginRequest;
import com.bank.dto.LoginResponse;
import com.bank.dto.RegisterRequest;
import com.bank.model.Customer;
import com.bank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    public AuthController(CustomerService customerService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> register(@Valid @RequestBody RegisterRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(request.getPassword());
        CustomerDTO dto = customerService.register(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Customer customer = customerService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(customer.getEmail());
        // FIX #1: LoginResponse = { token, customerId, fullName, email } — frontend stores all in localStorage
        LoginResponse response = new LoginResponse(
                token,
                customer.getId(),
                customer.getFullName(),
                customer.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof String)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = (String) auth.getPrincipal();
        CustomerDTO dto = customerService.getProfileByEmail(email);
        return ResponseEntity.ok(dto);
    }
}
