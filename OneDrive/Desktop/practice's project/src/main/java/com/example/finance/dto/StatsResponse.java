package com.example.finance.dto;
import lombok.Data;

@Data
public class StatsResponse {
    // За месяц
    private Double monthlyIncome = 0.0;
    private Double monthlyExpense = 0.0;

    // За неделю
    private Double weeklyIncome = 0.0;
    private Double weeklyExpense = 0.0;

    // За год
    private Double yearlyIncome = 0.0;
    private Double yearlyExpense = 0.0;
}