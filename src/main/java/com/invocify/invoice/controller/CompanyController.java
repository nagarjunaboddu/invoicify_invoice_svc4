package com.invocify.invoice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCompany(@RequestBody String companyString){

    }

}
