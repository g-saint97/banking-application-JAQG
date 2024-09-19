package com.jaqg.banking.dto;

import com.jaqg.banking.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        TransactionType type,
        Long fromAccount,
        Integer fromAccountSortCode,
        Long toAccount,
        Integer toAccountSortCode,
        BigDecimal amount) {
}