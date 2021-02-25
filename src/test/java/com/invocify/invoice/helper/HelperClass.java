package com.invocify.invoice.helper;

import com.invocify.invoice.entity.Company;

import java.util.UUID;

public class HelperClass {


    public static Company requestCompany(){
        return Company.builder().name("Amazon").address("233 Siliconvalley, CA").build();
    }

    public static Company expectedCompany() {
        return Company.builder().id(UUID.randomUUID()).name("Amazon").address("233 Siliconvalley, CA").build();
    }
}
