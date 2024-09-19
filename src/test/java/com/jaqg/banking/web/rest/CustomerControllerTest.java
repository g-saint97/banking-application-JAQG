package com.jaqg.banking.web.rest;


import com.jaqg.banking.dto.CustomerDTO;
import com.jaqg.banking.service.CustomerService;
import com.jaqg.banking.web.rest.CustomerController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void retrieveAllCustomers() {
        final var customerDto1 = new CustomerDTO(10L, "Charlie", List.of(235235L));
        final var customerDto2 = new CustomerDTO(11L, "Peter", List.of(235233L, 1L));

        when(customerService.retrieveAllCustomers()).thenReturn(List.of(customerDto1, customerDto2));

        try {
            mockMvc.perform(get("/customer").accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(customerDto1.id()))
                    .andExpect(jsonPath("$[0].fullName").value(customerDto1.fullName()))
                    .andExpect(jsonPath("$[0].accounts").isArray())
                    .andExpect(jsonPath("$[0].accounts", hasSize(1)))
                    .andExpect(jsonPath("$[1].id").value(customerDto2.id()))
                    .andExpect(jsonPath("$[1].fullName").value(customerDto2.fullName()))
                    .andExpect(jsonPath("$[1].accounts").isArray())
                    .andExpect(jsonPath("$[1].accounts", hasSize(customerDto2.accounts().size())))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCustomerByID() {
        final long customerId = 10L;
        final String fullName = "Charlie";
        List<Long> accounts = List.of(235235L);

        when(customerService.retrieveCustomer(customerId)).thenReturn(new CustomerDTO(customerId, fullName, accounts));

        try {
            mockMvc.perform(get("/customer/" + customerId).accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("id").value(customerId))
                    .andExpect(jsonPath("fullName").value(fullName))
                    .andExpect(jsonPath("accounts").isArray())
                    .andExpect(jsonPath("accounts", hasSize(1)))
                    .andExpect(jsonPath("accounts[0]").value(accounts.get(0)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addNewCustomer() {
        final long customerId = 10L;
        final String fullName = "Charlie";
        List<Long> accounts = List.of();

        when(customerService.createCustomer(fullName)).thenReturn(new CustomerDTO(customerId, fullName, accounts));

        try {
            mockMvc.perform(post("/customer")
                            .content(fullName)
                            .contentType(MediaType.TEXT_PLAIN)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("id").value(customerId))
                    .andExpect(jsonPath("fullName").value(fullName))
                    .andExpect(jsonPath("accounts").isArray())
                    .andExpect(jsonPath("accounts", hasSize(0)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteCustomer() {
        final long customerId = 10L;
        final BigDecimal value = new BigDecimal("50.55");

        when(customerService.deleteCustomer(customerId)).thenReturn(value);

        try {
            mockMvc.perform(delete("/customer/" + customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(value))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}