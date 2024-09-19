package com.jaqg.banking.repository;

import com.jaqg.banking.entity.RemoteAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RemoteAccountRepositoryTest {

    @Autowired
    private RemoteAccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    private RemoteAccount account;

    @BeforeEach
    void setUp() {
        RemoteAccount account = new RemoteAccount(5L, 1235);
        this.account = entityManager.persist(account);
    }

    @Test
    void testFindAllAccounts() {
        RemoteAccount account = new RemoteAccount(6L, 1235);

        entityManager.persist(account);

        List<RemoteAccount> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2).contains(account, this.account);
    }

    @Test
    void testFindAccountById() {
        Optional<RemoteAccount> optionalAccount = accountRepository.findByIdNumberAndIdSortCode(this.account.getNumber(), this.account.getSortCode());

        assertThat(optionalAccount).isPresent();

        RemoteAccount account = optionalAccount.get();

        assertThat(account.getNumber()).isEqualTo(this.account.getNumber());
        assertThat(account.getSortCode()).isEqualTo(this.account.getSortCode());
    }

    @Test
    void testSaveAccount() {
        final int expectedSourceCode = 12346;

        account.setSortCode(expectedSourceCode);
        final var updatedAccount = accountRepository.save(account);

        assertThat(updatedAccount.getNumber()).isEqualTo(account.getNumber());
        assertThat(updatedAccount.getSortCode()).isEqualTo(expectedSourceCode);
    }
}