package com.edou.es.entity;

/**
 * @ClassName Article
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/26 9:54
 * @Version 1.0
 */
public class Article {
    private Long id;
    private String title;

    public Article() {
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Article(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
