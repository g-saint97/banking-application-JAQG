package com.jaqg.banking.dto;

import com.jaqg.banking.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        LocalDateTime time,
        TransactionType type,
        Long fromAccount,
        Integer fromAccountSortCode,
        Long toAccount,
        Integer toAccountSortCode,
        BigDecimal amount) {
}