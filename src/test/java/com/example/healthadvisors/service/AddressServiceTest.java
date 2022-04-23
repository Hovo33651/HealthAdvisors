package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    private ExpectedException exceptionRule;

    @BeforeEach
    private void getExceptionRule() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> cl = Class.forName("org.junit.rules.ExpectedException");
        Constructor<?>[] declaredConstructors = cl.getDeclaredConstructors();
        ExpectedException exceptionRule = null;
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            declaredConstructor.setAccessible(true);
            exceptionRule = (ExpectedException) declaredConstructor.newInstance();
        }
        this.exceptionRule = exceptionRule;
    }




    @Test
    void save() {
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .country("Armenia")
                .region("Shirak")
                .city("Gyumri")
                .street("P Sevak")
                .address("18 g, 22")
                .build();
        Address address = addressService.save(createAddressRequest);
        assertNotNull(address);
        assertEquals("P Sevak", address.getStreet());
    }


    @Test
    void delete() throws Exception {
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .country("Armenia")
                .region("Shirak")
                .city(" ")
                .street("P Sevak")
                .address("18 g, 22")
                .build();
        Address address = addressService.save(createAddressRequest);
        assertNotNull(address);
        assertEquals("P Sevak", address.getStreet());

        addressService.delete(address);
        Optional<Address> byId = addressRepository.findById(address.getId());
        assertFalse(byId.isPresent());
    }

}
