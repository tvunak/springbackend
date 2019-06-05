package com.example.restexampletv.controllers;

import com.example.restexampletv.model.Article;
import com.example.restexampletv.model.ArticleImage;
import com.example.restexampletv.model.UploadFileResponse;
import com.example.restexampletv.repositories.ArticleRepository;
import com.example.restexampletv.services.ArticleService;
import com.example.restexampletv.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StorageService fileStorageService;

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



    @RequestMapping(value="/api/uploadFile/{id}",  method= RequestMethod.POST)
    public UploadFileResponse uploadFile(@RequestPart("file") MultipartFile file, @PathVariable long id) {
        String fileName = fileStorageService.storeFile(file);
        Article article = this.articleService.getArticle(id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        ArticleImage  articleImage = new ArticleImage(fileName, fileDownloadUri,
                file.getContentType(), file.getSize(), article);

        this.articleService.saveArticleImage(articleImage);
        System.out.println("The file uploaded is:");
        System.out.println(file.getOriginalFilename());
        System.out.println("The article id:");
        System.out.println(id);
        System.out.println("The file download url is:");
        System.out.println(fileDownloadUri);
        //System.out.println(id);

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @RequestMapping(value="/api/downloadFile/{fileName:.+}",  method= RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
          //  logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }




}
