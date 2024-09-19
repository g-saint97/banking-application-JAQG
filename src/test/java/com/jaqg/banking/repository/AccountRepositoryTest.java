package com.jaqg.banking.repository;

import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.entity.Transaction;
import com.jaqg.banking.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private LocalAccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Customer customer;
    private LocalAccount account;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setFullName("Peter Smith");

        this.customer = entityManager.persist(customer);

        LocalAccount account = new LocalAccount();
        account.setName("Checking");
        account.setOpeningBalance(BigDecimal.ONE);
        account.setBalance(BigDecimal.ONE);
        account.setSortCode(1235);
        account.setCustomer(this.customer);

        this.account = entityManager.persist(account);
    }

    @Test
    void testFindAllAccounts() {
        LocalAccount account = new LocalAccount();
        account.setName("Savings");
        account.setOpeningBalance(BigDecimal.TEN);
        account.setBalance(BigDecimal.TEN);
        account.setSortCode(1234);
        account.setCustomer(customer);

        entityManager.persist(account);

        List<LocalAccount> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2).contains(account, this.account);
    }

    @Test
    void testFindAccountById() {
        Optional<LocalAccount> optionalAccount = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(this.account.getNumber(), this.account.getSortCode());

        assertThat(optionalAccount).isPresent();

        LocalAccount account = optionalAccount.get();

        assertThat(account.getNumber()).isEqualTo(this.account.getNumber());
        assertThat(account.getName()).isEqualTo(this.account.getName());
        assertThat(account.getBalance()).isEqualTo(this.account.getBalance());
        assertThat(account.getSortCode()).isEqualTo(this.account.getSortCode());
        assertThat(account.getCustomer()).isEqualTo(this.customer);
        assertThat(account.getTransactions()).isEmpty();
    }

    @Test
    void testSaveAccount() {
        final String expectedName = "Checking";
        final BigDecimal expectedBalance = BigDecimal.ZERO;

        account.setName(expectedName);
        account.setBalance(expectedBalance);
        final var updatedAccount = accountRepository.save(account);

        assertThat(updatedAccount.getNumber()).isEqualTo(account.getNumber());
        assertThat(updatedAccount.getName()).isEqualTo(expectedName);
        assertThat(updatedAccount.getBalance()).isEqualTo(expectedBalance);
    }

    @Test
    void testFindAccountByIdWithDebitTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setRecipient(account);
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setAmount(BigDecimal.TEN);
        transaction1.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        account.addDebitTransaction(entityManager.persist(transaction1));
        LocalAccount storedAccount = entityManager.persist(account);


        Optional<LocalAccount> optionalAccount = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(storedAccount.getNumber(), storedAccount.getSortCode());
        assertThat(optionalAccount).isPresent();
        account = optionalAccount.get();
        assertThat(account.getNumber()).isEqualTo(storedAccount.getNumber());
        assertThat(account.getName()).isEqualTo(storedAccount.getName());
        assertThat(account.getBalance()).isEqualTo(storedAccount.getBalance());
        assertThat(account.getSortCode()).isEqualTo(storedAccount.getSortCode());
        assertThat(account.getTransactions()).hasSize(1).contains(transaction1);
    }

    @Test
    void testFindAccountByIdWithDebitTransaction_withJpaSaveMethod() {
        Transaction transaction1 = new Transaction();
        transaction1.setRecipient(account);
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setAmount(BigDecimal.TEN);
        transaction1.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        account.addDebitTransaction(transaction1);

        var savedAccount = accountRepository.save(account);

        assertThat(savedAccount.getNumber()).isEqualTo(account.getNumber());
        assertThat(savedAccount.getName()).isEqualTo(account.getName());
        assertThat(savedAccount.getBalance()).isEqualTo(account.getBalance());
        assertThat(savedAccount.getSortCode()).isEqualTo(account.getSortCode());

        List<Transaction> transactions = savedAccount.getTransactions();

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void testFindAccountByIdWithCreditTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setSender(account);
        transaction1.setType(TransactionType.WITHDRAWAL);
        transaction1.setAmount(BigDecimal.TEN);
        transaction1.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        account.addCreditTransaction(transaction1);

        LocalAccount storedAccount = entityManager.persist(account);

        Optional<LocalAccount> optionalAccount = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(storedAccount.getNumber(), storedAccount.getSortCode());
        assertThat(optionalAccount).isPresent();
        account = optionalAccount.get();
        assertThat(account.getNumber()).isEqualTo(storedAccount.getNumber());
        assertThat(account.getName()).isEqualTo(storedAccount.getName());
        assertThat(account.getBalance()).isEqualTo(storedAccount.getBalance());
        assertThat(account.getSortCode()).isEqualTo(storedAccount.getSortCode());
        assertThat(account.getTransactions()).hasSize(1).contains(transaction1);
    }

    @Test
    void testFindAccountByIdWithDebitAndCreditTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setRecipient(account);
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setAmount(BigDecimal.ONE);
        transaction1.setDateTime(LocalDateTime.now());

        account.addDebitTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setSender(account);
        transaction2.setType(TransactionType.WITHDRAWAL);
        transaction2.setAmount(BigDecimal.TEN);
        transaction2.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        account.addCreditTransaction(transaction2);

        LocalAccount storedAccount = entityManager.persist(account);

        Optional<LocalAccount> optionalAccount = accountRepository.findByIdNumberAndIdSortCodeAndIsClosedFalse(storedAccount.getNumber(), storedAccount.getSortCode());
        assertThat(optionalAccount).isPresent();
        account = optionalAccount.get();
        assertThat(account.getNumber()).isEqualTo(storedAccount.getNumber());
        assertThat(account.getName()).isEqualTo(storedAccount.getName());
        assertThat(account.getBalance()).isEqualTo(storedAccount.getBalance());
        assertThat(account.getSortCode()).isEqualTo(storedAccount.getSortCode());
        assertThat(account.getTransactions()).hasSize(2).contains(transaction1, transaction2);
    }
}