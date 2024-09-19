package com.jaqg.banking.mapper;

import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.entity.LocalAccount;

import java.util.List;

//Convert Account Entity to AccountResponseDTO
public class AccountMapper {

    public static List<AccountDTO> mapToDTO(List<LocalAccount> accounts) {
        return accounts.stream()
                .map(AccountMapper::mapToDTO)
                .toList();
    }

    public static AccountDTO mapToDTO(LocalAccount account) {
        return new AccountDTO(
                account.getNumber(),
                account.getSortCode(),
                account.getName(),
                account.getOpeningBalance(),
                TransactionsMapper.mapToDTO(account.getTransactions()), // convert to DTO,
                account.getBalance(),
                account.getCustomer().getId());
    }
}
