package com.jaqg.banking.web.rest;

import com.jaqg.banking.web.rest.BankController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BankController.class)
@TestPropertySource(properties = {"sortCode=4354"})
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getsortcode() {
        try {
            mockMvc.perform(get("/sortCode").accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(4354))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}