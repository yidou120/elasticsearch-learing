package com.edou.es.test;

import com.edou.es.SpringBootEsApplication;
import com.edou.es.entity.Article;
import com.edou.es.repository.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName SpringBootEsTest
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/27 9:09
 * @Version 1.0
 */
@SpringBootTest(classes = SpringBootEsApplication.class)
@RunWith(SpringRunner.class)
public class SpringBootEsTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void test(){
        Article article = new Article();
        article.setId(1);
        article.setTitle("test");
        article.setContent("testContent");
        articleRepository.save(article);
    }
}
