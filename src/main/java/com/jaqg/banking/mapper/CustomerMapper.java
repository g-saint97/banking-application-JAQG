package com.jaqg.banking.mapper;

import com.jaqg.banking.dto.CustomerDTO;
import com.jaqg.banking.entity.Account;
import com.jaqg.banking.entity.Customer;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getAccounts()
                        .stream()
                        .map(Account::getNumber)
                        .toList()
        );
    }
}
