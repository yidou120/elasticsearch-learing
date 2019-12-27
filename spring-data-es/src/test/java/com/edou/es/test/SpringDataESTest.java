package com.edou.es.test;

import com.edou.es.entity.Article;
import com.edou.es.repository.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName SpringDataESTest
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/26 15:33
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpringDataESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void createIndex(){
//        System.out.println("test");
        //创建index和mappings
        elasticsearchTemplate.createIndex(Article.class);
        elasticsearchTemplate.putMapping(Article.class);
//        ctrl+shift+空格
    }
    @Test
    public void saveArticle(){
        Article article = new Article();
        article.setId(1);
        article.setTitle("测试SpringData ElasticSearch");
        article.setContent("Spring Data ElasticSearch 基于 spring data API 简化 elasticSearch操作，将原始操作elasticSearch的客户端API 进行封装" +
                "Spring Data为Elasticsearch Elasticsearch项目提供集成搜索引擎");
        articleRepository.save(article);
    }
}
