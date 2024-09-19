package com.jaqg.banking.service.impl;

import com.jaqg.banking.dto.TransactionDTO;
import com.jaqg.banking.dto.TransactionRequestDTO;
import com.jaqg.banking.entity.Account;
import com.jaqg.banking.entity.RemoteAccount;
import com.jaqg.banking.entity.Transaction;
import com.jaqg.banking.exception.AccountNotFoundException;
import com.jaqg.banking.exception.NotEnoughFundsException;
import com.jaqg.banking.mapper.TransactionsMapper;
import com.jaqg.banking.repository.LocalAccountRepository;
import com.jaqg.banking.repository.RemoteAccountRepository;
import com.jaqg.banking.repository.TransactionRepository;
import com.jaqg.banking.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Integer sortCode;
    private final LocalAccountRepository accountRepository;
    private final RemoteAccountRepository remoteAccountRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    public TransactionServiceImpl(LocalAccountRepository accountRepository,
                                  RemoteAccountRepository remoteAccountRepository,
                                  TransactionRepository transactionRepository,
                                  @Value("${sortcode}") Integer sortCode,
                                  RestTemplateBuilder restTemplateBuilder) {
        this.accountRepository = accountRepository;
        this.remoteAccountRepository = remoteAccountRepository;
        this.transactionRepository = transactionRepository;
        this.sortCode = sortCode;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public TransactionDTO withdraw(TransactionRequestDTO request) {
        final var account = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(request.fromAccount(), request.fromAccountSortCode())
                .orElseThrow(() -> new AccountNotFoundException(request.fromAccount()));

        BigDecimal newBalance = account.getBalance().subtract(request.amount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFundsException(request.fromAccount());
        }

        Transaction transaction = new Transaction(request.amount(), request.type(), null, account);
        transaction = transactionRepository.save(transaction);

        account.setBalance(newBalance);
        accountRepository.save(account);

        return TransactionsMapper.mapToDTO(transaction);
    }

    @Override
    public TransactionDTO deposit(TransactionRequestDTO request) {
        final var account = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(request.toAccount(), request.toAccountSortCode())
                .orElseThrow(() -> new AccountNotFoundException(request.toAccount()));

        BigDecimal newBalance = account.getBalance().add(request.amount());
        Transaction transaction = new Transaction(request.amount(), request.type(), account, null);
        transactionRepository.save(transaction);

        account.setBalance(newBalance);
        accountRepository.save(account);

        return TransactionsMapper.mapToDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions(Long accountNumber) {
        final var account = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(accountNumber, sortCode)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return TransactionsMapper.mapToDTO(account.getTransactions());
    }

    @Override
    public TransactionDTO transfer(TransactionRequestDTO request) {
        Account sender = processFromAccount(request);
        Account recipient = processToAccount(request);

        Transaction transaction = new Transaction(request.amount(), request.type(), recipient, sender);
        transactionRepository.save(transaction);

        return TransactionsMapper.mapToDTO(transaction);
    }

    private Account processFromAccount(TransactionRequestDTO request) {
        if (Objects.equals(request.fromAccountSortCode(), sortCode)) {
            final var sender = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(request.fromAccount(), request.fromAccountSortCode())
                    .orElseThrow(() -> new AccountNotFoundException(request.fromAccount()));
            BigDecimal newBalance = sender.getBalance().subtract(request.amount());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new NotEnoughFundsException(request.fromAccount());
            }
            sender.setBalance(newBalance);
            return accountRepository.save(sender);
        } else {
            var sender = remoteAccountRepository.findByIdNumberAndIdSortCode(request.fromAccount(), request.fromAccountSortCode())
                    .orElse(new RemoteAccount(request.fromAccount(), request.fromAccountSortCode()));
            return remoteAccountRepository.save(sender);
        }
    }

    private Account processToAccount(TransactionRequestDTO request) {
        if (Objects.equals(request.toAccountSortCode(), sortCode)) {
            final var recipient = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(request.toAccount(), request.toAccountSortCode())
                    .orElseThrow(() -> new AccountNotFoundException(request.toAccount()));
            recipient.setBalance(recipient.getBalance().add(request.amount()));
            return accountRepository.save(recipient);
        } else {
            final var recipient = remoteAccountRepository.findByIdNumberAndIdSortCode(request.toAccount(), request.toAccountSortCode())
                    .orElse(new RemoteAccount(request.toAccount(), request.toAccountSortCode()));

            String url = "http://localhost:" + request.toAccountSortCode() + "/transaction";
            TransactionDTO response = restTemplate.postForObject(url, request, TransactionDTO.class);

            return remoteAccountRepository.save(recipient);
        }
    }
}
