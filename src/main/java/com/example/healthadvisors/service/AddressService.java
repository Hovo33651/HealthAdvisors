package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public Address save(CreateAddressRequest createAddressRequest){
        Address address = modelMapper.map(createAddressRequest, Address.class);
        return addressRepository.save(address);
    }

    public void delete(Address address){
        addressRepository.delete(address);
    }
}
