package com.example.restexampletv.services;

import com.example.restexampletv.model.Address;
import com.example.restexampletv.model.Article;
import com.example.restexampletv.model.Order;
import com.example.restexampletv.model.User;
import com.example.restexampletv.repositories.AddressRepository;
import com.example.restexampletv.repositories.OrderRepository;

import com.example.restexampletv.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Order addOrder(String body) {
        JSONObject jsonOrder;
        long userId,deliveryAddressId,billingAddressId;
        String articlesStr;
        try {
            jsonOrder = new JSONObject(body);
            userId = jsonOrder.getLong("userId");
            deliveryAddressId = jsonOrder.getLong("deliveryAddressId");
            billingAddressId = jsonOrder.getLong("billingAddressId");
            articlesStr = jsonOrder.getString("articles");
            System.out.println("order service");
            System.out.println(userId);
            System.out.println(deliveryAddressId);
            System.out.println(billingAddressId);
            User user = this.userRepository.getUserById(userId);

            JSONArray jArray = (JSONArray) new JSONTokener(articlesStr).nextValue();
            ObjectMapper om = new ObjectMapper();
            ArrayList<Article> articles = new ArrayList<Article>();
            for (int i= 0; i<jArray.length(); i++){
                System.out.println(jArray.getJSONObject(i));
                Article article = om.readValue(jArray.getJSONObject(i).toString(), Article.class);
                articles.add(article);
            }

            if (deliveryAddressId != billingAddressId){
                Address deliveryAddress = this.addressRepository.getOne(deliveryAddressId);
                Address billingAddress = this.addressRepository.getOne(billingAddressId);
                Order order = setOrder(user, deliveryAddress, billingAddress, articles);
                return order;
            }else{
                Address deliveryAddress = this.addressRepository.getOne(deliveryAddressId);
                Order order = setOrder(user, deliveryAddress, deliveryAddress, articles);
                return order;
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Order setOrder(User user, Address deliveryAddress, Address billingAddress, ArrayList<Article> articles){
        Order order = new Order();
        order.setUser(user);
        order.setArticles(articles);
        order.setBillingAddress(billingAddress);
        order.setDeliveryAddress(deliveryAddress);
        this.orderRepository.save(order);
        return order;
    }
}
