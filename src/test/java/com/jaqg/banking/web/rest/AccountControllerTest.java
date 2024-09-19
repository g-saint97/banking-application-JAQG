package com.jaqg.banking.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.dto.AccountRequestDTO;
import com.jaqg.banking.service.AccountService;
import com.jaqg.banking.web.rest.AccountController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(AccountController.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private List<AccountDTO> accountResponses;
    private AccountDTO accountResponse1;
    private AccountDTO accountResponse2;
    private AccountRequestDTO accountRequest;

    @BeforeEach
    void setup() {

        accountResponse1 = new AccountDTO(1234L, 1111,
                "Savings", new BigDecimal(100),
                new ArrayList<>(), new BigDecimal(100),
                1L);
        accountResponse2 = new AccountDTO(5678L, 2222,
                "Checkings", new BigDecimal(200),
                new ArrayList<>(), new BigDecimal(200),
                2L);

        accountResponses = new ArrayList<>();
        accountResponses.add(accountResponse1);
        accountResponses.add(accountResponse2);

        accountRequest = new AccountRequestDTO(1L, "Savings", new BigDecimal(100));
    }

    @Test
    void retrieveAllAccountsTest() {
        //Mock the service
        when(accountService.retrieveAllAccounts()).thenReturn(accountResponses); //returns list of AccountResponseDTO

        //Create GET request
        RequestBuilder request = MockMvcRequestBuilders
                .get("/account")
                .accept(MediaType.APPLICATION_JSON);

        //Perform the GET request and get result
        try {
            mockMvc.perform(request)
                    .andExpect(jsonPath("$", hasSize(2)))
//                    .andExpect(jsonPath("$[0].accounts").isArray())
                    .andExpect(jsonPath("$[0].number").value(accountResponse1.number()))
                    .andExpect(jsonPath("$[0].sortCode").value(accountResponse1.sortCode()))
                    .andExpect(jsonPath("$[0].name").value(accountResponse1.name()))
                    .andExpect(jsonPath("$[0].balance").value(accountResponse1.balance()))
                    .andExpect(jsonPath("$[0].transactions").value(accountResponse1.transactions()))
                    .andExpect(jsonPath("$[0].openingBalance").value(accountResponse1.openingBalance()))
                    .andExpect(jsonPath("$[0].customer").value(accountResponse1.customer()))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAccountByNumber() throws Exception {

        //Mock Service
        when(accountService.findAccountByNumber(accountResponse1.number())).thenReturn(accountResponse1);

        //Create GET Request
        RequestBuilder request = MockMvcRequestBuilders
                .get("/account/{number}", accountResponse1.number())
                .accept(MediaType.APPLICATION_JSON);

        //Perform Request

        try {
            mockMvc.perform(request)
                    .andExpect(jsonPath("number").value(accountResponse1.number()))
                    .andExpect(jsonPath("sortCode").value(accountResponse1.sortCode()))
                    .andExpect(jsonPath("name").value(accountResponse1.name()))
                    .andExpect(jsonPath("balance").value(accountResponse1.balance()))
                    .andExpect(jsonPath("transactions").value(accountResponse1.transactions()))
                    .andExpect(jsonPath("openingBalance").value(accountResponse1.openingBalance()))
                    .andExpect(jsonPath("customer").value(accountResponse1.customer()))
                    .andExpect(status().isOk())
                    .andReturn();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Test
    void createAccount() throws Exception {
        //Mock Service
        when(accountService.createAccount(accountRequest)).thenReturn(accountResponse1);

        //Convert DTO to JSON
        String accountRequestJson = objectMapper.writeValueAsString(accountRequest);

        //Create POST request
        RequestBuilder request = MockMvcRequestBuilders
                .post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountRequestJson)
                .accept(MediaType.APPLICATION_JSON);

        //Perform request and get response
        try {
            mockMvc.perform(request)
                    .andExpect(jsonPath("number").value(accountResponse1.number()))
                    .andExpect(jsonPath("sortCode").value(accountResponse1.sortCode()))
                    .andExpect(jsonPath("name").value(accountResponse1.name()))
                    .andExpect(jsonPath("balance").value(accountResponse1.balance()))
                    .andExpect(jsonPath("transactions").value(accountResponse1.transactions()))
                    .andExpect(jsonPath("openingBalance").value(accountResponse1.openingBalance()))
                    .andExpect(jsonPath("customer").value(accountResponse1.customer()))
                    .andExpect(status().isCreated()) //expect 201 created
                    .andReturn();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }


    @Test
    void closeAccount() throws Exception {
        Long accountNumber = accountResponse1.number();
        BigDecimal balance = accountResponse1.balance();

        //Mock Service
        when(accountService.closeAccount(accountNumber)).thenReturn(balance);

        //Create DELETE Request
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/account/{number}", accountNumber)
                .accept(MediaType.APPLICATION_JSON);

        //Perform request and get response

        try {
            mockMvc.perform(request)
                    .andExpect(jsonPath("$").value(balance))
                    .andExpect(status().isOk()) //200 OK Status
                    .andReturn();

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

}



