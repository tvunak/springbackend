package com.example.restexampletv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.*;

// telling spring jpa that this class is a table and table name is users
@Entity
@Table(name="users")
public class User implements UserDetails{

    //setting primary key of the table
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String username;
    private String email;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String password;
    private String name;
    private String middleName;
    private String lastName;

    private boolean isAdmin;
    private boolean enabled;
    @JsonIgnore
    private boolean nonExpired;
    @JsonIgnore
    private boolean nonLocked;
    @JsonIgnore
    private boolean credentialsNonExpired;

    private static String ROLE_PREFIX = "ROLE_";





    public User(){}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = true;
        this.nonExpired = true;
        this.nonLocked = true;
        this.credentialsNonExpired = true;
        this.isAdmin = false;

    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.nonExpired ;
    }

    @Override
    public boolean isAccountNonLocked() {
        return  this.nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled ;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        if (this.isAdmin()){
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX +"ADMIN"));
        }else{
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX +"USER"));
        }

        return list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


}
