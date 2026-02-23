package com.example.finance.service;

import com.example.finance.dto.TransactionRequest;
import com.example.finance.model.*;
import com.example.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import com.example.finance.dto.StatsResponse;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final AccountRepository accountRepo;
    private final TransactionRepository transRepo;

    // Создание счета
    public Account createAccount(Account account) {
        if (account.getBalance() == null) account.setBalance(0.0);
        return accountRepo.save(account);
    }

    // Главная логика: добавить транзакцию и обновить баланс
    @Transactional
    public Transaction addTransaction(TransactionRequest request) {
        Account account = accountRepo.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        // Обновляем баланс
        if (request.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() + request.getAmount());
        } else if (request.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - request.getAmount());
        }

        accountRepo.save(account); // Сохраняем новый баланс

        // Создаем и сохраняем транзакцию
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);
        transaction.setCategory(request.getCategory()); // <-- Добавь эту строку перед save
        return transRepo.save(transaction);
    }
    public StatsResponse getStats() {
        List<Transaction> all = transRepo.findAll();
        StatsResponse stats = new StatsResponse();
        LocalDate now = LocalDate.now();

        for (Transaction t : all) {
            if (t.getDate() == null) continue;
            LocalDate tDate = t.getDate().toLocalDate(); // Преобразуем LocalDateTime в LocalDate

            boolean isIncome = t.getType() == TransactionType.INCOME;
            double amount = t.getAmount();

            // Проверка за Месяц
            if (tDate.getMonth() == now.getMonth() && tDate.getYear() == now.getYear()) {
                if (isIncome) stats.setMonthlyIncome(stats.getMonthlyIncome() + amount);
                else stats.setMonthlyExpense(stats.getMonthlyExpense() + amount);
            }

            // Проверка за Неделю (последние 7 дней)
            if (tDate.isAfter(now.minusDays(7))) {
                if (isIncome) stats.setWeeklyIncome(stats.getWeeklyIncome() + amount);
                else stats.setWeeklyExpense(stats.getWeeklyExpense() + amount);
            }

            // Проверка за Год
            if (tDate.getYear() == now.getYear()) {
                if (isIncome) stats.setYearlyIncome(stats.getYearlyIncome() + amount);
                else stats.setYearlyExpense(stats.getYearlyExpense() + amount);
            }
        }
        return stats;
    }
}