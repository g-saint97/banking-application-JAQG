package com.jaqg.banking.service;

import com.jaqg.banking.dto.TransactionDTO;
import com.jaqg.banking.dto.TransactionRequestDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO withdraw(TransactionRequestDTO request);

    TransactionDTO transfer(TransactionRequestDTO request);

    TransactionDTO deposit(TransactionRequestDTO request);

    List<TransactionDTO> getAllTransactions(Long accountNumber);
}
