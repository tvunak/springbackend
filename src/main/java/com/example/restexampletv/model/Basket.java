package com.example.restexampletv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id")
    private User user;

    private String date;

    @ManyToMany
    @JoinTable(name = "basket_article", joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    List<Article> addedArticles = new ArrayList<>();

    public Basket() {
    }

    public Basket(User user) {
        this.user = user;
        this.date = new Date().toString();
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

    public List<Article> getAddedArticles() {
        return addedArticles;
    }

    public void setAddedArticles(Article addedArticle) {
        this.addedArticles.add(addedArticle);
    }

    public void removeArticle(Article article){
        this.addedArticles.remove(article);
    }
}
