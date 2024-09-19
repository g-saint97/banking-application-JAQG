package com.jaqg.banking.repository;

import com.jaqg.banking.entity.LocalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalAccountRepository extends JpaRepository<LocalAccount, Long> {

    List<LocalAccount> findByIdSortCodeAndIsClosedFalse(int sortCode);

    Optional<LocalAccount> findByIdNumberAndIdSortCodeAndIsClosedFalse(long id, int sortCode);

}
