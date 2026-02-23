package com.example.finance.dto;
import com.example.finance.model.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private String description;
    private Double amount;
    private TransactionType type;
    private Long accountId; // ID счета, к которому привязана операция
    private String category;
}