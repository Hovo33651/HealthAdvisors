package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {

    @NotBlank(message = "Choose your country")
    private String country;

    @NotBlank(message = "Choose your region")
    private String region;

    @NotBlank(message = "Choose your city")
    private String city;

    @NotBlank(message = "Write you street name")
    @NotNull
    private String street;

    @NotBlank(message = "Write your house address")
    @NotNull(message = "Write your house address")
    private String address;

}
