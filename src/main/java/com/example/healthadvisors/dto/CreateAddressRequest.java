package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    @NotBlank(message = "Choose your country")
    private String country;

    @NotBlank(message = "Choose your region")
    private String region;

    @NotBlank(message = "Choose your city")
    private String city;

    @NotBlank(message = "Write you street name")
    private String street;

    @NotBlank(message = "Write your house address")
    private String address;

}
