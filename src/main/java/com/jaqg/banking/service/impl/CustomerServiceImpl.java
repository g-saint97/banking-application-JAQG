package com.jaqg.banking.service.impl;

import com.jaqg.banking.dto.CustomerDTO;
import com.jaqg.banking.entity.Customer;
import com.jaqg.banking.exception.CustomerNotFoundException;
import com.jaqg.banking.mapper.CustomerMapper;
import com.jaqg.banking.repository.CustomerRepository;
import com.jaqg.banking.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    // Implementing Get Request DTO

    @Override
    public CustomerDTO retrieveCustomer(Long id) {
        Customer customer = customerRepo.findByIdAndIsRemovedFalse(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return CustomerMapper.toDTO(customer);
    }

    // Implementing Post Request DTO
    @Override
    public CustomerDTO createCustomer(String fullName) {
        Customer customer = customerRepo.save(new Customer(fullName));
        return CustomerMapper.toDTO(customer);
    }

    // Implementing Delete Request DTO
    @Override
    public BigDecimal deleteCustomer(Long id) {
        Customer customer = customerRepo.findByIdAndIsRemovedFalse(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setRemoved(true);

        BigDecimal totalBalance = BigDecimal.ZERO;
        for (var account : customer.getAccounts()) {
            totalBalance = totalBalance.add(account.getBalance());
            account.setBalance(BigDecimal.ZERO);
            account.setClosed(true);
        }
        customerRepo.save(customer);
        return totalBalance;
    }

    @Override
    public List<CustomerDTO> retrieveAllCustomers() {
        return customerRepo.findByIsRemovedFalse()
                .stream()
                .map(CustomerMapper::toDTO)
                .toList();
    }
}




