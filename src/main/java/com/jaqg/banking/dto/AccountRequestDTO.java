package com.jaqg.banking.dto;

import java.math.BigDecimal;

public record AccountRequestDTO(
        Long customerId,
        String accountName,
        BigDecimal openingBalance
) {
}
