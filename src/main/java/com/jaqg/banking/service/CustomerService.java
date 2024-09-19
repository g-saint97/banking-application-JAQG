package com.jaqg.banking.service;

import com.jaqg.banking.dto.CustomerDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    CustomerDTO retrieveCustomer(Long id);

    CustomerDTO createCustomer(String fullName);

    BigDecimal deleteCustomer(Long id);

    List<CustomerDTO> retrieveAllCustomers();

}
