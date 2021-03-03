package com.invocify.invoice.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySV {

    private String name;
    private String city;
    private String state;
}
