package com.jaqg.banking.service;

import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.dto.AccountRequestDTO;
import com.jaqg.banking.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {

    List<AccountDTO> retrieveAllAccounts();

    AccountDTO createAccount(AccountRequestDTO createAccountDTO);

    BigDecimal closeAccount(long number) throws AccountNotFoundException;

    AccountDTO findAccountByNumber(long number) throws AccountNotFoundException;

}
