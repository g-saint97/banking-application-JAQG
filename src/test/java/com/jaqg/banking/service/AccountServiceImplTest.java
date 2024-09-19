package com.jaqg.banking.service;

import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.dto.AccountRequestDTO;
import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.repository.CustomerRepository;
import com.jaqg.banking.repository.LocalAccountRepository;
import com.jaqg.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private LocalAccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepo;

    private AccountService accountService;

    private LocalAccount account;
    private List<LocalAccount> accounts;
    private Customer customer;
    private AccountDTO accountResponse;
    private AccountRequestDTO accountRequest;

    @BeforeEach
    void setUp() {
        //Create accounts
        accountService = new AccountServiceImpl(accountRepository, customerRepo,1234);

        customer = new Customer("Joyceline Marealle");

        accounts = new ArrayList<>();
        account = new LocalAccount("Savings", new BigDecimal("100"), customer, 1111);
        account.setNumber(564234567L);
        LocalAccount account2 = new LocalAccount("Checkings", new BigDecimal("200"), customer, 2222);
        account.setNumber(456345635L);
        //add to list
        accounts.add(account);
        accounts.add(account2);

        //Create accountDTOs that match the account above
        accountResponse = new AccountDTO(1234L, 1111, "Savings", new BigDecimal(100), new ArrayList<>(), new BigDecimal(100), 1L);

        //Create accountRequest DTO
        accountRequest = new AccountRequestDTO(1L, "Savings", new BigDecimal("100"));
    }


    @Test
    void retrieveAllAccounts() {

        //Test the size and elements of List
        when(accountRepository.findByIdSortCodeAndIsClosedFalse(1234)).thenReturn(accounts);

        //Execute service method
        List<AccountDTO> accountDTOList = accountService.retrieveAllAccounts();

        //Assertions
        assertThat(accountDTOList).hasSize(2);
//        assertThat(accountDTOList).containsExactly(accountResponse1, accountResponse2);

        //Test for each attribute of account
//        assertEquals(accountResponse1.number(), accountDTOList.get(0).number());
        assertEquals(accountResponse.name(), accountDTOList.get(0).name());
        assertEquals(accountResponse.openingBalance(), accountDTOList.get(0).openingBalance());
        assertEquals(accountResponse.balance(), accountDTOList.get(0).balance());
        assertEquals(accountResponse.sortCode(), accountDTOList.get(0).sortCode());

        //Verify mock if method called
        verify(accountRepository).findByIdSortCodeAndIsClosedFalse(1234);
    }

    @Test
    void findAccountByNumber() {

        Long accountNumber = account.getNumber();
        Optional<LocalAccount> optionalAccount = Optional.ofNullable(account);

        //Mock findById()
        when(accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(accountNumber, 1234)).thenReturn(optionalAccount);

        //Execute service method
        AccountDTO account = accountService.findAccountByNumber(accountNumber);

        //Asserts results
        assertThat(this.account.getNumber()).isEqualTo(account.number());
        assertThat(this.account.getOpeningBalance()).isEqualTo(account.openingBalance());
        assertThat(this.account.getBalance()).isEqualTo(account.balance());
        assertThat(this.account.getSortCode()).isEqualTo(account.sortCode());

        //Verify Mock
        verify(accountRepository).findByIdNumberAndIdSortCodeAndIsClosedFalse(accountNumber, 1234);

    }

    @Test
    void closeAccount() {
        Long accountNumber = account.getNumber();
        Optional<LocalAccount> optionalAccount = Optional.ofNullable(account);

        //Mock findById()
        when((accountRepository).findByIdNumberAndIdSortCodeAndIsClosedFalse(accountNumber, 1234)).thenReturn(optionalAccount);

        //Capture original balance
        BigDecimal originalBalance = account.getBalance();

        //Call service method
        BigDecimal closingBalance = accountService.closeAccount(accountNumber);

        //Assert results
        //closing balance equals to original account1 balance
        assertThat(originalBalance).isEqualTo(closingBalance);

        //closed account balance is zero
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);

        //Verify mock used
        verify(accountRepository).findByIdNumberAndIdSortCodeAndIsClosedFalse(accountNumber, 1234);
        verify(accountRepository).save(account);


    }

    @Test
    void createAccount() {
        Long customerId = accountRequest.customerId();
        Optional<Customer> optionalCustomer = Optional.ofNullable(customer);

        //mock customerRepo.findBy
        when(customerRepo.findByIdAndIsRemovedFalse(customerId)).thenReturn(optionalCustomer);
        when(accountRepository.save(any(LocalAccount.class))).thenReturn(account);

        //assert
        AccountDTO accountResponse = accountService.createAccount(accountRequest);

        assertThat(accountResponse.name()).isEqualTo(accountRequest.accountName());
        assertThat(accountResponse.openingBalance()).isEqualTo(accountRequest.openingBalance());

        //Verify
        verify(customerRepo).findByIdAndIsRemovedFalse(customerId);
    }
}
