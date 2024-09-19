package com.jaqg.banking.service.impl;

import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.dto.AccountRequestDTO;
import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.exception.AccountNotFoundException;
import com.jaqg.banking.exception.CustomerNotFoundException;
import com.jaqg.banking.exception.NotEnoughFundsException;
import com.jaqg.banking.mapper.AccountMapper;
import com.jaqg.banking.repository.CustomerRepository;
import com.jaqg.banking.repository.LocalAccountRepository;
import com.jaqg.banking.service.AccountService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.jaqg.banking.mapper.AccountMapper.mapToDTO;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final LocalAccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final Integer sortCode;

    public AccountServiceImpl(
            LocalAccountRepository accountRepository,
            CustomerRepository customerRepository,
            @Value("${sortcode}") Integer sortCode
    ) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.sortCode = sortCode;
    }

    @Override
    public List<AccountDTO> retrieveAllAccounts() {
        logger.info("Getting accounts from Account Repository");

        //Retrieve all accounts from the repository
        List<LocalAccount> accounts = accountRepository.findByIdSortCodeAndIsClosedFalse(sortCode);

        //Convert each Account Entity to a AccountResponseDTO
        return mapToDTO(accounts);
    }

    @Override
    public AccountDTO createAccount(AccountRequestDTO accountRequestDTO) throws AccountNotFoundException {

        //Validate opening balance
        if (accountRequestDTO.openingBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFundsException("Opening balance cannot be negative ");
        }

        //find customer by id then create Account
        Customer customer = customerRepository.findByIdAndIsRemovedFalse(accountRequestDTO.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(accountRequestDTO.customerId()));

        //save an account to database
        LocalAccount account = accountRepository.save(new LocalAccount(accountRequestDTO.accountName(), accountRequestDTO.openingBalance(), customer, sortCode));
        return AccountMapper.mapToDTO(account);
    }

    @Override
    public BigDecimal closeAccount(long number) throws AccountNotFoundException {
        LocalAccount account = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(number, sortCode)
                .orElseThrow(() -> new AccountNotFoundException(number));
        BigDecimal balance = account.getBalance();
        account.setBalance(BigDecimal.ZERO);
        account.setClosed(true);
        accountRepository.save(account);
        return balance;
    }

    @Override
    public AccountDTO findAccountByNumber(long number) throws AccountNotFoundException {
        LocalAccount account = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(number, sortCode)
                .orElseThrow(() -> new AccountNotFoundException(number));
        return AccountMapper.mapToDTO(account);
    }

}
