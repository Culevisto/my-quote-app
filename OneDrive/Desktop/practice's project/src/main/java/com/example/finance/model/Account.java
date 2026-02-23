package com.example.finance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double balance = 0.0;
    private String color;

    // Новое поле: Цель накопления (если 0 или null - это обычный счет)
    private Double goalAmount = 0.0;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL) // При удалении счета удалятся и транзакции
    private List<Transaction> transactions = new ArrayList<>();
}