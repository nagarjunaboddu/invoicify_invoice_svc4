package com.invocify.invoice.controller;

import com.invocify.invoice.entity.Company;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invocify/companies")
public class CompanyController {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company){
        company.setId(UUID.randomUUID());
        return company;
    }

}
