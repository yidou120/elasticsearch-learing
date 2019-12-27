package com.edou.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @ClassName Article
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/27 8:55
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "blog4",type = "article")
public class Article {
    @Id
    @Field(type = FieldType.Integer,store = true,index = false)
    private Integer id;
    @Field(index = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.text)
    private String title;
    @Field(index = true,analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.text)
    private String content;
}
