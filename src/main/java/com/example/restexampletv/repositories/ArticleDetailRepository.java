package com.example.restexampletv.repositories;

import com.example.restexampletv.model.ArticleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDetailRepository extends JpaRepository<ArticleDetail, Long> {
        ArticleDetail getArticleDetailById(Long id);
}
