package com.jaqg.banking.repository;

import com.jaqg.banking.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setFullName("Peter Smith");

        Customer customer2 = new Customer();
        customer2.setFullName("Josh Smith");

        entityManager.persist(customer1);
        entityManager.persist(customer2);

        List<Customer> customers = customerRepo.findAll();
        assertThat(customers).hasSize(2).contains(customer1, customer2);
    }

    @Test
    void testFindAllCustomersAndIsRemovedFalse() {
        Customer customer1 = new Customer();
        customer1.setFullName("Peter Smith");
        customer1.setRemoved(true);

        Customer customer2 = new Customer();
        customer2.setFullName("Josh Smith");

        entityManager.persist(customer1);
        entityManager.persist(customer2);

        List<Customer> customers = customerRepo.findByIsRemovedFalse();
        assertThat(customers).hasSize(1).contains(customer2);
    }

    @Test
    void testFindCustomerById() {
        Customer customer1 = new Customer();
        customer1.setFullName("Peter Smith");

        Customer customer2 = new Customer();
        customer2.setFullName("Josh Smith");

        entityManager.persist(customer1);
        Customer storedCustomer = entityManager.persist(customer2);

        Optional<Customer> optionalCustomer = customerRepo.findById(storedCustomer.getId());
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getId()).isEqualTo(storedCustomer.getId());
        assertThat(customer.getFullName()).isEqualTo(storedCustomer.getFullName());
        assertThat(customer.isRemoved()).isEqualTo(false);
        assertThat(customer.getAccounts()).isEmpty();
    }

    @Test
    void testFindCustomerByIdAndIsRemovedFalse() {
        Customer customer1 = new Customer();
        customer1.setFullName("Peter Smith");
        customer1.setRemoved(true);

        Customer customer2 = new Customer();
        customer2.setFullName("Josh Smith");

        Customer storedCustomer1 = entityManager.persist(customer1);
        Customer storedCustomer2 = entityManager.persist(customer2);

        Optional<Customer> optionalCustomer1 = customerRepo.findByIdAndIsRemovedFalse(storedCustomer1.getId());
        assertThat(optionalCustomer1).isNotPresent();

        Optional<Customer> optionalCustomer2 = customerRepo.findByIdAndIsRemovedFalse(storedCustomer2.getId());
        assertThat(optionalCustomer2).isPresent();
        Customer customer = optionalCustomer2.get();
        assertThat(customer.getId()).isEqualTo(storedCustomer2.getId());
        assertThat(customer.getFullName()).isEqualTo(storedCustomer2.getFullName());
        assertThat(customer.isRemoved()).isEqualTo(false);
        assertThat(customer.getAccounts()).isEmpty();
    }

    @Test
    void testSaveCustomer() {
        final String expectedName = "Jason Jackson";

        Customer expectedCustomer = new Customer();
        expectedCustomer.setFullName(expectedName);

        Customer customer = customerRepo.save(expectedCustomer);

        assertThat(customer.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(customer.getFullName()).isEqualTo(expectedCustomer.getFullName());
    }

    @Test
    void testMarkCustomerForDeletion() {
        final String expectedName = "Josh Smith";

        Customer customer = new Customer();
        customer.setFullName(expectedName);

        final Customer storedCustomer = entityManager.persist(customer);

        customer.setRemoved(true);
        customer = customerRepo.save(customer);

        assertThat(customer.getId()).isEqualTo(storedCustomer.getId());
        assertThat(customer.getFullName()).isEqualTo(expectedName);
        assertThat(customer.isRemoved()).isEqualTo(true);
    }

}