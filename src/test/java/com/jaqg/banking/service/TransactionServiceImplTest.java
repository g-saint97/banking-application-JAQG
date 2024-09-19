package com.jaqg.banking.service;

import com.jaqg.banking.dto.TransactionDTO;
import com.jaqg.banking.dto.TransactionRequestDTO;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.enums.TransactionType;
import com.jaqg.banking.repository.LocalAccountRepository;
import com.jaqg.banking.repository.RemoteAccountRepository;
import com.jaqg.banking.repository.TransactionRepository;
import com.jaqg.banking.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private final static Integer SORT_CODE = 1234;

    @Mock
    private LocalAccountRepository accountRepository;

    @Mock
    private RemoteAccountRepository remoteAccountRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    private TransactionRequestDTO transferRequest;
    private TransactionRequestDTO withdrawRequest;
    private TransactionRequestDTO depositRequest;

    @BeforeEach
    void setup() {
//        transactionService = new TransactionServiceImpl(accountRepository, remoteAccountRepository, transactionRepository, SORT_CODE, restTemplateBuilder);
//
//        when(restTemplateBuilder.build()).thenReturn(restTemplate);
//
//        when(accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(any(), any())).thenReturn(
//                Optional.of(new LocalAccount("Savings", new BigDecimal("100"), null, 4444))
//        );
        transferRequest = new TransactionRequestDTO(
                TransactionType.TRANSFER,
                123456789L,
                1234,
                987654321L,
                4444,
                new BigDecimal("150.00"));
        withdrawRequest = new TransactionRequestDTO(
                TransactionType.WITHDRAWAL,
                123456789L,
                1234,
                null,
                null,
                new BigDecimal(100)
        );
        depositRequest = new TransactionRequestDTO(
                TransactionType.DEPOSIT,
                null,
                null,
                123456789L,
                1234,
                new BigDecimal(100)
        );
    }


    @Test
    void deposit() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        transactionService = new TransactionServiceImpl(accountRepository, remoteAccountRepository, transactionRepository, SORT_CODE, restTemplateBuilder);

        when(accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(any(), any())).thenReturn(Optional.of(new LocalAccount("Savings", new BigDecimal("100"), null, 4444)));

        TransactionDTO response = transactionService.deposit(transferRequest);


        assertEquals(response.amount(), transferRequest.amount());

        verify(accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(transferRequest.toAccount(), transferRequest.toAccountSortCode()));
        verify(accountRepository.save(any()));
    }


    @Test
    void withdraw() {
        TransactionDTO response = transactionService.withdraw(withdrawRequest);

        assertEquals(response.amount(), withdrawRequest.amount());
        verify(accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(withdrawRequest.fromAccount(), withdrawRequest.toAccountSortCode()));
        verify(accountRepository.save(any()));
    }


    @Test
    void transfer() {
    }
}