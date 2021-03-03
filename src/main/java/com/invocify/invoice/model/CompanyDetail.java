package com.invocify.invoice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetail extends CompanySV {

    private UUID id;
    private String street;
    private String postalCode;


    public CompanyDetail(String name, String city, String state, UUID id, String street, String postalCode) {
        super(name, city, state);
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
    }
}
