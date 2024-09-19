package com.jaqg.banking.web.rest;

import com.jaqg.banking.dto.CustomerDTO;
import com.jaqg.banking.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDTO> retrieveAllCustomers() {
        return this.customerService.retrieveAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerByID(@PathVariable Long id) {
        return customerService.retrieveCustomer(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addNewCustomer(@RequestBody String fullName) {
        return customerService.createCustomer(fullName);
    }

    @DeleteMapping("/{id}")
    public BigDecimal deleteCustomer(@PathVariable("id") Long ID) {
        return customerService.deleteCustomer(ID);
    }

}
