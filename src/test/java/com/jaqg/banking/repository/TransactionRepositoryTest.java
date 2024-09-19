package com.jaqg.banking.repository;

import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.entity.Transaction;
import com.jaqg.banking.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class TransactionRepositoryTest {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;


    @ParameterizedTest
    @EnumSource(TransactionType.class)
    void testOperationType(TransactionType type) {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setDateTime(LocalDateTime.now());
        expectedTransaction.setType(type);
        expectedTransaction.setAmount(BigDecimal.TEN);

        final long id = entityManager.persist(expectedTransaction).getId();

        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        assertThat(transactionOptional).isPresent();
        Transaction transaction = transactionOptional.get();
        assertEquals(type, transaction.getType());
    }

    @Test
    void testFindAllTransactions() {
        Customer customer = new Customer();
        customer.setFullName("Peter Smith");

        customer = entityManager.persist(customer);

        LocalAccount account = new LocalAccount();
        account.setName("Checking");
        account.setOpeningBalance(BigDecimal.ONE);
        account.setBalance(BigDecimal.ONE);
        account.setSortCode(1235);
        account.setCustomer(customer);
        account = entityManager.persist(account);

        Transaction transaction1 = new Transaction();
        transaction1.setSender(account);
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setAmount(BigDecimal.TEN);
        transaction1.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        Transaction transaction2 = new Transaction();
        transaction2.setRecipient(account);
        transaction2.setType(TransactionType.WITHDRAWAL);
        transaction2.setAmount(BigDecimal.ONE);
        transaction2.setDateTime(LocalDateTime.now());

        entityManager.persist(transaction1);
        entityManager.persist(transaction2);

        List<Transaction> Transactions = transactionRepository.findAll();
        assertThat(Transactions).hasSize(2).contains(transaction1, transaction2);
    }

    @Test
    void testFindTransactionById() {
        Transaction transaction1 = new Transaction();
        transaction1.setRecipient(new LocalAccount());
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setAmount(BigDecimal.TEN);
        transaction1.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        Transaction transaction2 = new Transaction();
        transaction2.setSender(new LocalAccount());
        transaction2.setType(TransactionType.WITHDRAWAL);
        transaction2.setAmount(BigDecimal.ONE);
        transaction2.setDateTime(LocalDateTime.now());

        entityManager.persist(transaction1);
        Transaction storedTransaction = entityManager.persist(transaction2);

        Optional<Transaction> optionalTransaction = transactionRepository.findById(storedTransaction.getId());
        assertThat(optionalTransaction).isPresent();
        Transaction transaction = optionalTransaction.get();
        assertThat(transaction.getId()).isEqualTo(storedTransaction.getId());
        assertThat(transaction.getDateTime()).isEqualTo(storedTransaction.getDateTime());
        assertThat(transaction.getAmount()).isEqualTo(storedTransaction.getAmount());
        assertThat(transaction.getType()).isEqualTo(storedTransaction.getType());
    }

    @Test
    void testSaveTransaction() {
        final TransactionType expectedType = TransactionType.TRANSFER;
        final BigDecimal expectedValue = BigDecimal.ONE;

        Transaction transaction = new Transaction();
        transaction.setRecipient(new LocalAccount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setDateTime(LocalDateTime.of(2024, 6, 2, 23, 34, 34));

        final Transaction storedTransaction = entityManager.persist(transaction);

        transaction.setType(expectedType);
        transaction.setAmount(expectedValue);
        transaction = transactionRepository.save(transaction);

        assertThat(transaction.getId()).isEqualTo(storedTransaction.getId());
        assertThat(transaction.getType()).isEqualTo(expectedType);
        assertThat(transaction.getAmount()).isEqualTo(expectedValue);
    }
}