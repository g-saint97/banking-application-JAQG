package com.jaqg.banking.mapper;

import com.jaqg.banking.dto.TransactionDTO;
import com.jaqg.banking.entity.Transaction;

import java.util.List;

public class TransactionsMapper {

    //Convert List of Transaction Entity to List of TransactionResponseDTO
    public static List<TransactionDTO> mapToDTO(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionsMapper::mapToDTO)
                .toList(); //collect to a list
    }

    public static TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getDateTime(),
                transaction.getType(),
                transaction.getFromAccountNumber(),
                transaction.getFromAccountSourceCode(),
                transaction.getToAccountNumber(),
                transaction.getToAccountSourceCode(),
                transaction.getAmount());

    }
}
