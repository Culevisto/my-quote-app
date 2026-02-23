package com.example.finance.controller;

import com.example.finance.dto.StatsResponse;
import com.example.finance.dto.TransactionRequest;
import com.example.finance.model.Transaction;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {
    private final TransactionRepository repository;
    private final FinanceService service;
    @GetMapping
    public List<Transaction> getAll(@RequestParam(required = false) Long accountId) {
        if (accountId != null) {
            // Если ID пришел, фильтруем по счету
            return repository.findByAccountIdOrderByDateDesc(accountId);
        }
        // Если ID нет, возвращаем всё
        return repository.findAllByOrderByDateDesc();
    }

    @PostMapping
    public Transaction create(@RequestBody TransactionRequest request) {
        return service.addTransaction(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // Упростим: пока просто удаляем запись без отката баланса
        repository.deleteById(id);
    }
    @GetMapping("/stats")
    public StatsResponse getStats() {
        return service.getStats();
    }

}