package com.example.restexampletv.controllers;

import com.example.restexampletv.model.Address;
import com.example.restexampletv.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    @RequestMapping(value= "/api/address", method= RequestMethod.POST)
    public Address addAddress(@RequestBody String frontendAddress){
        return this.addressService.addAddress(frontendAddress);
    }
    @RequestMapping(value= "/api/address/{userId}", method= RequestMethod.GET)
    public List<Address> getAddress(@PathVariable long userId){
        return this.addressService.getAddress(userId);
    }

    @RequestMapping(value= "/api/address/all", method= RequestMethod.GET)
    public List<Address> getAddresses(){
        return this.addressService.getAllAddresses();
    }

    @RequestMapping(value="/api/address", method= RequestMethod.DELETE)
    public String removeArticle(@RequestBody Long id){
        System.out.println();
        System.out.println("remove address with ID: "+id.toString());
        return this.addressService.deleteAddress(id);
    }

    @RequestMapping(value="/api/address", method= RequestMethod.PUT)
    public Address removeArticle(@RequestBody String body){
        System.out.println();
        System.out.println(body);
        System.out.println("update address address ");
        return this.addressService.updateAddress(body);
    }
}
