package com.example.finance.controller;

import com.example.finance.model.Account;
import com.example.finance.repository.AccountRepository;
import com.example.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {
    private final AccountRepository repository;
    private final FinanceService service;

    @GetMapping
    public List<Account> getAll() {
        // Сортируем по ID, чтобы порядок всегда был один и тот же (сначала старые)
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        return service.createAccount(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        repository.deleteById(id);
    }

}