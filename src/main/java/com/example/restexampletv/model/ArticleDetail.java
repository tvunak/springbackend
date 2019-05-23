package com.example.restexampletv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class ArticleDetail{
    @Id
    private long id;
    private String description;
    private String manufacturer;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Article article;

    public ArticleDetail() {
    }
    public ArticleDetail(long id, String description, String manufacturer){
        this.id = id;
        this.description = description;
        this.manufacturer = manufacturer;
    }

    public ArticleDetail(Article article) {
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
