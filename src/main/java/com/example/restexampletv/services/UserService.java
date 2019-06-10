package com.example.restexampletv.services;

import com.example.restexampletv.model.Basket;
import com.example.restexampletv.model.User;
import com.example.restexampletv.repositories.BasketRepository;
import com.example.restexampletv.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private BasketRepository basketRepository;

    public User addUser(User frontendUser){
        User user = new User();
        user.setUsername(frontendUser.getUsername());
        user.setEmail(frontendUser.getEmail());
        user.setPassword(passwordEncoder.encode(frontendUser.getPassword()));
        user.setName(frontendUser.getName());
        user.setMiddleName(frontendUser.getMiddleName());
        user.setLastName(frontendUser.getLastName());
        user.setAdmin(false);
        this.userRepository.save(user);
        Basket basket = new Basket(user);
        this.basketRepository.save((basket));
        return user;
    }

    public ResponseEntity getAllUsers(){
        List<User> userList = new ArrayList<>();
        userList = this.userRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(mapper.writeValueAsString(userList));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONArray newArray = new JSONArray();
        for(int n = 0; n < jsonArray.length(); n++)
        {
            JSONObject object = null;
            try {
                object = jsonArray.getJSONObject(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            object.remove("password");
            object.remove("admin");
            newArray.put(object);
        }
        return new ResponseEntity<String>(newArray.toString(), HttpStatus.OK);
    }

    public String deleteUser(String body){
        String username= "", email= "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
            username = jsonObject.getString("username");
            email = jsonObject.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        User user = this.userRepository.findOneByUsername(username);
        System.out.println(username);
        System.out.println(email);
        if (user != null){
            System.out.println(user.toString());
            if (user.getEmail().equals(email)){
                this.userRepository.deleteById(user.getId());
                return "{\"message\": \"User deleted\"}";
            }
        }

        return "{\"message\": \"Requested user not found \"}";
    }

    public String updateUser(String body){
        try {
            System.out.println(body);
            JSONObject jsonBody = new JSONObject(body);
            Long userId = jsonBody.getLong("id");
            String username = jsonBody.getString("username");
            String email = jsonBody.getString("email");

            String name = jsonBody.getString("name");
            String middleName = jsonBody.getString("middleName");
            String lastName = jsonBody.getString("lastName");

            User user = this.userRepository.getUserById(userId);
            if (user != null){
                if (!Objects.equals(username, "")){
                    user.setUsername(username);
                }
                if (!Objects.equals(email, "")){
                    user.setEmail(email);
                }
                if (!Objects.equals(name, "")){
                    user.setName(name);
                }
                if (!Objects.equals(lastName, "")){
                    user.setLastName(lastName);
                }
                user.setMiddleName(middleName);
                this.userRepository.save(user);
            }else{
                return "{\"message\": \"Requested user not found \"}";
            }
            return "{\"message\": \"User updated\"}";
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"message\": \"Requested user not found \"}";
        }
    }

    public User getUserProfile(String body) {
        System.out.println(body);
        String username = "";
        try {
            JSONObject jsonObject = new JSONObject(body);
            username = jsonObject.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.userRepository.findOneByUsername(username);


    }
}
