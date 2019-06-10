package com.example.restexampletv.services;


import com.example.restexampletv.model.Address;
import com.example.restexampletv.model.User;
import com.example.restexampletv.repositories.AddressRepository;
import com.example.restexampletv.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public Address addAddress(String frontendAddress){
        long userId =0;
        JSONObject jsonAddress;
        Address address= null;
        try {
            jsonAddress = new JSONObject(frontendAddress);
            userId = jsonAddress.getLong("user");
            ObjectMapper om = new ObjectMapper();
            address = om.readValue(frontendAddress, Address.class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address != null){
            User user = this.userRepository.getUserById(userId);
            address.setUser(user);
            return this.addressRepository.save(address);
        }else{
            return null;
        }


    }

    public List<Address> getAddress(long  userId) {
        return this.addressRepository.getAddressesByUserId(userId);
    }

    public String deleteAddress(Long id) {
            this.addressRepository.deleteById(id);
            return "{\"message\": \"Article deleted\"}";

    }

    public Address updateAddress(String body) {
        try {
            JSONObject jsonAddress = new JSONObject(body);
            long id = jsonAddress.getLong("id");
            String city = jsonAddress.getString("city");
            String country = jsonAddress.getString("country");
            String houseNumber = jsonAddress.getString("houseNumber");
            String street = jsonAddress.getString("street");
            int zip = jsonAddress.getInt("zip");
            System.out.println("ID from frontend");
            System.out.println(id);
            Address address = this.addressRepository.getOne(id);
            address.setCity(city);
            address.setCountry(country);
            address.setHouseNumber(houseNumber);
            address.setStreet(street);
            address.setZip(zip);
            this.addressRepository.save(address);
            return address;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Address> getAllAddresses() {
        return this.addressRepository.findAll();
    }
}
