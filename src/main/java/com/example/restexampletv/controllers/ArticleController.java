package com.example.restexampletv.controllers;

import com.example.restexampletv.model.Article;
import com.example.restexampletv.repositories.ArticleRepository;
import com.example.restexampletv.services.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public ArticleController(ArticleRepository articleRepository){
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value= "/api/articles", method= RequestMethod.GET)
    public List<Article> articles(){
        return this.articleService.getAllArticles();
    }

    @RequestMapping(value= "/api/article/{id}", method= RequestMethod.GET)
    public Article getArticle(@PathVariable long id)  {
        return this.articleService.getArticle(id);
    }

    @RequestMapping(value= "/api/articleDetails/{id}", method= RequestMethod.GET)
    public String getArticleDetails(@PathVariable long id) throws JsonProcessingException, JSONException {
        return this.articleService.getArticleWithDetails(id);
    }

    @RequestMapping(value="/api/article", method= RequestMethod.POST, consumes = "application/json")
    public List<Article> addArticle(@RequestBody String article, @RequestHeader HttpHeaders headers) throws JsonProcessingException {
        System.out.println("Controler addArticle");
        System.out.println(headers.get("Authorization").toString());

        this.articleService.addNewArticle(article);

        //just for testing --> returning all articles
        return this.articleService.getAllArticles();
    }

    @RequestMapping(value="/api/article", method= RequestMethod.DELETE)
    public String removeArticle(@RequestBody Long id){
        System.out.println();
        System.out.println("remove article with ID:"+id.toString());
        return this.articleService.deleteArticle(id);
    }

    @RequestMapping(value="/api/article", method= RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public Article updateArticle(@RequestBody String jsonStr){
        return this.articleService.updateArticle(jsonStr);
    }


}
