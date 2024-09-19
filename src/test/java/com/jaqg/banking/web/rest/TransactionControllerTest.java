package com.jaqg.banking.web.rest;


import com.jaqg.banking.config.JacksonConfiguration;
import com.jaqg.banking.dto.TransactionRequestDTO;
import com.jaqg.banking.dto.TransactionDTO;
import com.jaqg.banking.enums.TransactionType;
import com.jaqg.banking.service.TransactionService;
import com.jaqg.banking.web.rest.TransactionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(JacksonConfiguration.class)
@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createDeposit() {
        final TransactionDTO transactionDTO = new TransactionDTO(
                LocalDateTime.of(2024, 5, 4, 12, 5, 6),
                TransactionType.DEPOSIT,
                null,
                null,
                1L,
                1234,
                new BigDecimal("55.65")
        );

        final String payload = "{" +
                "  \"type\": \"" + transactionDTO.type() + "\"" +
                ", \"fromAccount\": " + transactionDTO.fromAccount() +
                ", \"fromAccountSortCode\": " + transactionDTO.fromAccountSortCode() +
                ", \"toAccount\": " + transactionDTO.toAccount() +
                ", \"toAccountSortCode\": " + transactionDTO.toAccountSortCode() +
                ", \"amount\": " + transactionDTO.amount() +
                "}";

        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenReturn(transactionDTO);

        try {
            mockMvc.perform(post("/transaction")
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("time").value("04-05-2024 12:05:06"))
                    .andExpect(jsonPath("type").value(transactionDTO.type().name()))
                    .andExpect(jsonPath("fromAccount").value(transactionDTO.fromAccount()))
                    .andExpect(jsonPath("fromAccountSortCode").value(transactionDTO.fromAccountSortCode()))
                    .andExpect(jsonPath("toAccount").value(transactionDTO.toAccount()))
                    .andExpect(jsonPath("toAccountSortCode").value(transactionDTO.toAccountSortCode()))
                    .andExpect(jsonPath("amount").value(transactionDTO.amount()))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createWithdrawal() {
        final TransactionDTO transactionDTO = new TransactionDTO(
                LocalDateTime.of(2024, 5, 4, 12, 5, 6),
                TransactionType.WITHDRAWAL,
                1L,
                1234,
                null,
                null,
                new BigDecimal("55.65")
        );

        final String payload = "{" +
                "  \"type\": \"" + transactionDTO.type() + "\"" +
                ", \"fromAccount\": " + transactionDTO.fromAccount() +
                ", \"fromAccountSortCode\": " + transactionDTO.fromAccountSortCode() +
                ", \"toAccount\": " + transactionDTO.toAccount() +
                ", \"toAccountSortCode\": " + transactionDTO.toAccountSortCode() +
                ", \"amount\": " + transactionDTO.amount() +
                "}";

        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenReturn(transactionDTO);

        try {
            mockMvc.perform(post("/transaction")
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("time").value("04-05-2024 12:05:06"))
                    .andExpect(jsonPath("type").value(transactionDTO.type().name()))
                    .andExpect(jsonPath("fromAccount").value(transactionDTO.fromAccount()))
                    .andExpect(jsonPath("fromAccountSortCode").value(transactionDTO.fromAccountSortCode()))
                    .andExpect(jsonPath("toAccount").value(transactionDTO.toAccount()))
                    .andExpect(jsonPath("toAccountSortCode").value(transactionDTO.toAccountSortCode()))
                    .andExpect(jsonPath("amount").value(transactionDTO.amount()))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTransfer() {
        final TransactionDTO transactionDTO = new TransactionDTO(
                LocalDateTime.of(2024, 5, 4, 12, 5, 6),
                TransactionType.TRANSFER,
                1L,
                1234,
                2L,
                1234,
                new BigDecimal("55.65")
        );

        final String payload = "{" +
                "  \"type\": \"" + transactionDTO.type() + "\"" +
                ", \"fromAccount\": " + transactionDTO.fromAccount() +
                ", \"fromAccountSortCode\": " + transactionDTO.fromAccountSortCode() +
                ", \"toAccount\": " + transactionDTO.toAccount() +
                ", \"toAccountSortCode\": " + transactionDTO.toAccountSortCode() +
                ", \"amount\": " + transactionDTO.amount() +
                "}";

        when(transactionService.transfer(any(TransactionRequestDTO.class))).thenReturn(transactionDTO);

        try {
            mockMvc.perform(post("/transaction")
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("time").value("04-05-2024 12:05:06"))
                    .andExpect(jsonPath("type").value(transactionDTO.type().name()))
                    .andExpect(jsonPath("fromAccount").value(transactionDTO.fromAccount()))
                    .andExpect(jsonPath("fromAccountSortCode").value(transactionDTO.fromAccountSortCode()))
                    .andExpect(jsonPath("toAccount").value(transactionDTO.toAccount()))
                    .andExpect(jsonPath("toAccountSortCode").value(transactionDTO.toAccountSortCode()))
                    .andExpect(jsonPath("amount").value(transactionDTO.amount()))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}