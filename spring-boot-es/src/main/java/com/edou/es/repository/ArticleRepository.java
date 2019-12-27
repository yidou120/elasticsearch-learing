package com.edou.es.repository;

import com.edou.es.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ClassName ArticleRepository
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/27 9:01
 * @Version 1.0
 */
public interface ArticleRepository extends ElasticsearchRepository<Article,Integer> {
    List<Article> findByTitle(String title);
    List<Article> findByTitle(String title, Pageable pageable);
}
