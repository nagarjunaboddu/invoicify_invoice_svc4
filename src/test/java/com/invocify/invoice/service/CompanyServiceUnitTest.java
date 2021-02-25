package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.helper.HelperClass;


import com.invocify.invoice.repository.CompanyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceUnitTest {


   @InjectMocks
   private CompanyService companyService;

   @Mock
   private CompanyRepository companyRepository;





   @Test
    public void createCompany(){
       when(companyRepository.save(any(Company.class))).thenReturn((HelperClass.expectedCompany()));
       Company actual = companyService.createCompany(HelperClass.requestCompany());

       assertNotNull(actual.getId());
       assertEquals("Amazon", actual.getName());
       assertEquals("233 Siliconvalley, CA", actual.getAddress());
      verify(companyRepository, times(1)).save(any(Company.class));
   }
}
