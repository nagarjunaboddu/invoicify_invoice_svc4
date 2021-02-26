package com.invocify.invoice.controller;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invocify/companies")
@AllArgsConstructor
@Tag(name = "Company")
public class CompanyController {

    private CompanyService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company){
        return service.createCompany(company);
    }

}
