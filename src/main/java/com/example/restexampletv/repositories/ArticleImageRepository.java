package com.example.restexampletv.repositories;

import com.example.restexampletv.model.ArticleDetail;
import com.example.restexampletv.model.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
    ArticleDetail getArticleImageById(Long id);
}
