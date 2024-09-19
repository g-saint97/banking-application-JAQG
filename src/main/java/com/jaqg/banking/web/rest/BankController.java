package com.jaqg.banking.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sortCode")
@CrossOrigin
public class BankController {

    private final Integer sortCode;

    public BankController(@Value("${sortcode}") Integer sortCode) {
        this.sortCode = sortCode;
    }

    @GetMapping
    public Integer getSortCode() {
        return sortCode;
    }

}
