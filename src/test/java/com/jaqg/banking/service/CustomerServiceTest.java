package com.jaqg.banking.service;


import com.jaqg.banking.dto.CustomerDTO;
import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.entity.LocalAccount;
import com.jaqg.banking.repository.CustomerRepository;
import com.jaqg.banking.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void createCustomerTest() {
        var testCustomer = new Customer("Dan Jones");
        given(customerRepository.save(testCustomer)).willReturn((testCustomer));
        CustomerDTO customerDTO = customerService.createCustomer("Dan Jones");
        assertThat(customerDTO.fullName()).isEqualTo("Dan Jones");
        assertThat(customerDTO.accounts()).isEmpty();
        verify(customerRepository).save(testCustomer);
    }

    @Test
    public void deleteCustomerTest() {
        var testCustomer = new Customer("Dan Jones");
        testCustomer.addAccount(new LocalAccount("Test", BigDecimal.TEN, testCustomer, 1234));
        testCustomer.addAccount(new LocalAccount("Test2", BigDecimal.ONE, testCustomer, 1234));

        given(customerRepository.findByIdAndIsRemovedFalse(2L)).willReturn(Optional.of(testCustomer));

        BigDecimal amount = customerService.deleteCustomer(2L);

        assertThat(amount).isEqualTo(new BigDecimal("11"));

        verify(customerRepository).findByIdAndIsRemovedFalse(2L);
    }

    @Test
    public void retrieveAllCustomersTest() {
        List<Customer> customers = List.of(new Customer("Dan Jones"), new Customer("Ivan Jones"));
        final var expectedValues = List.of(new CustomerDTO(null, "Dan Jones", List.of()), new CustomerDTO(null, "Ivan Jones", List.of()));

        when(customerRepository.findByIsRemovedFalse()).thenReturn(customers);

        List<CustomerDTO> customerDTOS = customerService.retrieveAllCustomers();

        assertThat(customerDTOS).hasSize(2).containsAll(expectedValues);

        verify(customerRepository).findByIsRemovedFalse();
    }

    @Test
    void retrieveCustomerTest() {
        var testCustomer = new Customer("Dan Jones");

        LocalAccount account1 = new LocalAccount("Test", BigDecimal.TEN, testCustomer, 1234);
        account1.setNumber(12356236L);
        LocalAccount account2 = new LocalAccount("Test2", BigDecimal.ONE, testCustomer, 1234);
        account2.setNumber(46326236L);

        testCustomer.addAccount(account1);
        testCustomer.addAccount(account2);

        given(customerRepository.findByIdAndIsRemovedFalse(2L)).willReturn(Optional.of(testCustomer));

        CustomerDTO customerDTO = customerService.retrieveCustomer(2L);

        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.fullName()).isEqualTo("Dan Jones");
        assertThat(customerDTO.accounts()).isNotEmpty().containsAll(List.of(account1.getNumber(), account2.getNumber()));

        verify(customerRepository).findByIdAndIsRemovedFalse(2L);
    }
}
