package com.example.restexampletv.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Article{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String date;
    private String time;
    private float price;

    @JsonIgnore
    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
    private ArticleDetail articleDetail;

    @ManyToMany(mappedBy = "addedArticles")
    List<Basket> groups = new ArrayList<>();

    public Article(){}

    public Article( String name, String date, String time, float price) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArticleDetail getArticleDetail() {
        return articleDetail;
    }

    public void setArticleDetail(ArticleDetail articleDetail) {
        this.articleDetail = articleDetail;

    }
}
