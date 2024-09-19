package com.jaqg.banking.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountDTO(
        Long number,
        Integer sortCode,
        String name,
        BigDecimal openingBalance,
        List<TransactionDTO> transactions,
        BigDecimal balance,
        Long customer) {
}
