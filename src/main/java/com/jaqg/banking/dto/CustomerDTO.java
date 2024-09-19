package com.jaqg.banking.dto;

import java.util.List;

public record CustomerDTO(Long id, String fullName, List<Long> accounts) {
}


