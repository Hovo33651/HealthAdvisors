package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public void save(Address address){
        addressRepository.save(address);
    }

    public void delete(Address address){
        addressRepository.delete(address);
    }
}
