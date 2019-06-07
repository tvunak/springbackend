package com.example.restexampletv.services;

import com.example.restexampletv.model.Article;
import com.example.restexampletv.model.ArticleDetail;
import com.example.restexampletv.model.ArticleImage;
import com.example.restexampletv.repositories.ArticleDetailRepository;
import com.example.restexampletv.repositories.ArticleImageRepository;
import com.example.restexampletv.repositories.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleDetailRepository articleDetailRepository;

    @Autowired
    private ArticleImageRepository articleImageRepository;

    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }

    public Article getArticle(Long id){
        return articleRepository.getArticleById(id);
    }
    public String getArticleWithDetails(Long id) throws JsonProcessingException, JSONException {
        ObjectMapper om = new ObjectMapper();
        ArticleDetail details = articleDetailRepository.getArticleDetailById(id);
        Article article = articleRepository.getArticleById(id);
        String detailsString = om.writeValueAsString(details);
        String articleString = om.writeValueAsString(article);
        JSONObject jsonArticle = new JSONObject(articleString);
        jsonArticle.put("articleDetail",detailsString);
        System.out.println(jsonArticle.toString());
        return jsonArticle.toString();

    }

    public Article addNewArticle(String articleWithDetails) throws JsonProcessingException {
        System.out.println();
        System.out.println("article service");
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writeValueAsString(articleWithDetails));
        Article articleObject = new Article();
        try {
            // cast string to json
            JSONObject jsonArticle = new JSONObject(articleWithDetails);
            // creating article
            articleObject = om.readValue(articleWithDetails, Article.class);

            // fetch article detail from initial string
            String articleDetailString = jsonArticle.getString("articleDetail");
            ArticleDetail articleDetail = om.readValue(articleDetailString, ArticleDetail.class);

            articleDetail.setArticle(articleObject);
            articleObject.setArticleDetail(articleDetail);
            articleDetailRepository.save(articleDetail);
            articleRepository.save(articleObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ArticleDetail articleDetail = new ArticleDetail(article);
        //article.setArticleDetail(articleDetail);

        return articleObject;
    }

    public String deleteArticle(Long id){
        articleRepository.deleteById(id);
        return "{\"message\": \"Article deleted\"}";
    }

    public Article updateArticle(String jsonStr){
        try {
            System.out.println(jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            Long id = Long.parseLong(jsonObject.getString("id"));
            String name = jsonObject.getString("name");
            String date = jsonObject.getString("date");
            String time = jsonObject.getString("time");
            float price = Float.parseFloat(jsonObject.getString("price"));

            Article article = this.articleRepository.getArticleById(id);
            if (article != null){
                if (!Objects.equals(name, "")){
                    article.setName(name);
                }
                if (!Objects.equals(date, "")){
                    article.setDate(date);
                }
                if (!Objects.equals(time, "")){
                    article.setTime(time);
                }
                if (!Objects.equals(price, 0)){
                    article.setPrice(price);
                }
                articleRepository.save(article);
                return article;
            }

        } catch (JSONException e) {
            System.out.println(e);
            return null;
        }
        return null;
    }

    public void saveArticleImage(ArticleImage image){
        this.articleImageRepository.save(image);
    }



}
