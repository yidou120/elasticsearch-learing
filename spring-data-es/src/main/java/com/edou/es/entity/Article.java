package com.edou.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.nio.file.FileAlreadyExistsException;

/**
 * @ClassName Article
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/26 14:36
 * @Version 1.0
 */
@Document(indexName = "blog3",type = "article")
public class Article {
    @Id
    @Field(type = FieldType.Integer,store = true,index = false)
    private Integer id;
    @Field(index = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.text)
    private String title;
    @Field(index = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.text)
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
