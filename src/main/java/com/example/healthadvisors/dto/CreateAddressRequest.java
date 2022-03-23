package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
