package com.jaqg.banking.repository;

import com.jaqg.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByIsRemovedFalse();

    Optional<Customer> findByIdAndIsRemovedFalse(long id);

    boolean existsByIdAndIsRemovedFalse(Long aLong);
}
