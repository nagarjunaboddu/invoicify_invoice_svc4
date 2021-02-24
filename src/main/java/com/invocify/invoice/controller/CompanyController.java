package com.invocify.invoice.controller;

import com.invocify.invoice.model.Company;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company){
        company.setId(1L);
        return company;
    }

}
