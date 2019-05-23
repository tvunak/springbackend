package com.example.restexampletv.repositories;

import com.example.restexampletv.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> getAddressesByUserId(long userId);

}
