package com.example.restexampletv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    private String date;
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    //@JoinColumn(name = "address_id", insertable=false, updatable=false)
    private Address billingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    //@JoinColumn(name = "address_id", insertable=false, updatable=false)
    private Address deliveryAddress;

    @ManyToMany()
    @JoinTable(name = "order_article", joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<Article> articleList;

    public Order() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<Article> getArticles() {
        return articleList;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articleList = articles;
    }
}
