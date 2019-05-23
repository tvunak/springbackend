package com.example.restexampletv.controllers;

import com.example.restexampletv.model.Order;
import com.example.restexampletv.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value= "/api/order", method= RequestMethod.POST)
    public Order newOrder(@RequestBody String body){
        System.out.println("this was trigered");
        System.out.println(body);
        return this.orderService.addOrder(body);
    }
}
