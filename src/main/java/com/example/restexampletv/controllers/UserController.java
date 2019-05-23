package com.example.restexampletv.controllers;

import com.example.restexampletv.model.User;
import com.example.restexampletv.repositories.UserRepository;
import com.example.restexampletv.services.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository){
    }

    //endpoint created for testing purposes
    @GetMapping(produces = "application/json")
    @RequestMapping({ "/api/validateLogin" })
    public String validateLogin() {
        return "User successfully authenticated";
    }

    //endpoint for creating new user
    @RequestMapping(value= "/api/user/add", method= RequestMethod.POST)
    public User addUser(@RequestBody User frontendUser){
        return this.userService.addUser(frontendUser);
    }

    // endpoint for getting all users
    @RequestMapping(value = "/api/user", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getUsers() throws JsonProcessingException, JSONException {
        return this.userService.getAllUsers();
    }

    // endpoint for deleting user using Id
    @RequestMapping(value = "/api/user", method = RequestMethod.DELETE)
    public String deleteUser(@RequestBody String body){
        return this.userService.deleteUser(body);
    }

    // endpoint for updating user
    @RequestMapping(value="/api/user", method = RequestMethod.PUT)
    public String updateUser(@RequestBody String body){
        return this.userService.updateUser(body);
    }

    // endpoint for getting information about one user
    @RequestMapping(value = "/api/user/profile", method = RequestMethod.POST, produces = "application/json")
    public User getUserProfile(@RequestBody String body) {
        System.out.println(body);
        return this.userService.getUserProfile(body);
    }

}
