package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {

}
