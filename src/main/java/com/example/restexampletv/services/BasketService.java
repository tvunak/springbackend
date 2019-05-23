package com.example.restexampletv.services;
import com.example.restexampletv.model.Article;
import com.example.restexampletv.model.Basket;
import com.example.restexampletv.repositories.ArticleRepository;
import com.example.restexampletv.repositories.BasketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

@Service
public class BasketService {
    @Autowired
    BasketRepository basketRepository;

    @Autowired
    ArticleRepository articleRepository;

    public Article addToBasket(String data) {
        System.out.println(data);
        try {
            JSONObject json = new JSONObject(data);
            long userId = json.getLong("userId");
            long articleId = json.getLong("articleId");
            Basket basket = this.basketRepository.getByUserId(userId);
            Article article = this.articleRepository.getArticleById(articleId);
            basket.setAddedArticles(article);
            this.basketRepository.save(basket);
            return article;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<Article> getBasketItems(long userId) {
        Basket basket = this.basketRepository.getByUserId(userId);
        return basket.getAddedArticles();
    }

    public List<Article> deleteBasketItem(String data) {
        System.out.println(data);
        try {
            JSONObject json = new JSONObject(data);
            long userId = json.getLong("userId");
            long articleId = json.getLong("articleId");
            Basket basket = this.basketRepository.getByUserId(userId);
            Article article = this.articleRepository.getArticleById(articleId);
            basket.removeArticle(article);
            this.basketRepository.save(basket);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
