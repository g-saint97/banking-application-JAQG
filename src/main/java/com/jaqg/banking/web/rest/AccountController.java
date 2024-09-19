package com.jaqg.banking.web.rest;

import com.jaqg.banking.dto.AccountDTO;
import com.jaqg.banking.dto.AccountRequestDTO;
import com.jaqg.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    //constructor injection
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.retrieveAllAccounts();
    }

    @GetMapping("/{number}")
    public AccountDTO findAccountByNumber(@PathVariable Long number) {
        return accountService.findAccountByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@RequestBody AccountRequestDTO createAccountDTO) {
        return accountService.createAccount(createAccountDTO);
    }

    @DeleteMapping("/{number}")
    public BigDecimal closeAccount(@PathVariable Long number) {
        return accountService.closeAccount(number);
    }

}

