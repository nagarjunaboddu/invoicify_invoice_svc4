package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.helper.HelperClass;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceUnitTest {



    CompanyService companyService;


    @BeforeEach
    public void init(){
        companyService = new CompanyService();
    }


   @Test
    public void createCompany(){


       Company actual = companyService.createCompany(HelperClass.requestCompany());

       assertNotNull(actual.getId());
   }
}
