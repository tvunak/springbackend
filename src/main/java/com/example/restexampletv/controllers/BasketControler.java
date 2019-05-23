package com.example.restexampletv.controllers;

import com.example.restexampletv.model.Article;
import com.example.restexampletv.services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BasketControler {
    @Autowired
    BasketService basketService;

    @RequestMapping(value= "/api/basket", method= RequestMethod.POST)
    public Article addToBasket(@RequestBody String data){
        return this.basketService.addToBasket(data);
    }

    @RequestMapping(value= "/api/basket/{id}", method= RequestMethod.GET)
    public List<Article> getBasketItems(@PathVariable long id){
        return this.basketService.getBasketItems(id);
    }

    @RequestMapping(value= "/api/basket", method= RequestMethod.DELETE)
    public List<Article> deleteBasketItem(@RequestBody String data){
        return this.basketService.deleteBasketItem(data);
    }
}
