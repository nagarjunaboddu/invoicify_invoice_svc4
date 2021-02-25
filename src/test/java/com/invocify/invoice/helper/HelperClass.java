package com.invocify.invoice.helper;

import com.invocify.invoice.entity.Company;

public class HelperClass {

    public static Company requestCompany(){
        return Company.builder().name("Amazon").address("233 Siliconvalley, CA").build();
    }
}
