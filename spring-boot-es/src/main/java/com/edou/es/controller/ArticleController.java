package com.edou.es.controller;

import com.edou.es.dto.ResponseDto;
import com.edou.es.entity.Article;
import com.edou.es.repository.ArticleRepository;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName ArticleController
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/27 9:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/es")
@AllArgsConstructor
@Api(value = "springdata-es的增删改查",tags = "spring-data-es")
public class ArticleController {

    private ArticleRepository articleRepository;
    private ElasticsearchTemplate elasticsearchTemplate;

    //增加
    @ApiOperation(value = "保存一条数据",notes = "传入article")
    @PostMapping("/save")
    public ResponseDto<Article> save(@RequestBody Article article){
        articleRepository.save(article);
        ResponseDto<Article> responseDto = new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setSuccess(true);
        responseDto.setData(article);
        return responseDto;
    }
    //查询
    @ApiOperation(value = "根据id查询",notes = "传入id")
    @GetMapping("/find")
    public ResponseDto<Article> find(Integer id){
        Optional<Article> byId = articleRepository.findById(id);
        if(byId.isPresent()){
            return ResponseDto.getInstance(200,true,byId.get());
        }
        return ResponseDto.getInstance(404,false,null);
    }
    //删除
    @ApiOperation(value = "删除一条",notes = "传入id")
    @PostMapping("/delete")
    public ResponseDto<Article> delete(Integer id){
        articleRepository.delete(Article.builder().id(id).build());
        return ResponseDto.getInstance(200,true,null);
    }
    //删除
    @ApiOperation(value = "删除一条",notes = "传入title")
    @PostMapping("/deleteByTitle")
    public ResponseDto<Article> delete(String title){
        articleRepository.delete(Article.builder().title(title).build());
        return ResponseDto.getInstance(200,true,null);
    }
    //分页查询
    @ApiOperation(value = "分页查",notes = "传入分页参数")
    @GetMapping("/findPage")
    public ResponseDto findAll(int page,int size){
        PageRequest of = PageRequest.of(page-1, size);
        Page<Article> all = articleRepository.findAll(of);
        if(all.hasContent()){
            return ResponseDto.getInstance(200,true,all.getContent());
        }
        return ResponseDto.getInstance(400,false,null);
    }
    //批量插入
    @ApiOperation(value = "批量save",notes = "无参")
    @GetMapping("/batchSave")
    public ResponseDto saveBatch(){
        for(int i=1;i<101;i++){
            Article build = Article.builder().id(i).title(i + "elasticSearch 3.0版本发布..，更新").content(i + "ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口").build();
            articleRepository.save(build);
        }
        return ResponseDto.getInstance(200,true,null);
    }

    //根据title查找
    @ApiOperation(value = "根据title查",notes = "传入title")
    @GetMapping("/find/{title}")
    public ResponseDto findByTitle(@PathVariable String title){
        Iterable<Article> title1 = articleRepository.search(QueryBuilders.queryStringQuery(title).defaultField("title"));
        List<Article> lists = new ArrayList<>();
        title1.forEach(lists::add);
        if(!CollectionUtils.isEmpty(lists)){
            return ResponseDto.getInstance(200,true,lists);
        }
        return ResponseDto.getInstance(400,false,null);
        /*List<Article> byTitle = articleRepository.findByTitle(title);
        if(!CollectionUtils.isEmpty(byTitle)){
            return ResponseDto.getInstance(200,true,byTitle);
        }
        return ResponseDto.getInstance(400,false,null);*/
    }
    //根据title分页查询
    @ApiOperation(value = "根据title分页查",notes = "传入分页参数与title")
    @GetMapping("/find/{title}/{page}/{size}")
    public ResponseDto findByTitle(String title,int page,int size){
        List<Article> lists = new ArrayList<>();
        PageRequest of = PageRequest.of(page-1, size);
        //使用springdataes封装的查
      /*  List<Article> byTitle = articleRepository.findByTitle(title, of);
        if(!CollectionUtils.isEmpty(byTitle)){
            return ResponseDto.getInstance(200,true,byTitle);
        }
        return ResponseDto.getInstance(400,false,null);*/
        //使用querybuild查
        /*Page<Article> title1 = articleRepository.search(QueryBuilders.termQuery("title", title), of);
        title1.forEach(article -> lists.add(article));
        if(!CollectionUtils.isEmpty(lists)){
            return ResponseDto.getInstance(200,true,lists);
        }
        return ResponseDto.getInstance(400,false,null);*/
        //使用estemplate查
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(title)
                        .defaultField("title"))
                        .withPageable(of)
                        .build();
        elasticsearchTemplate.queryForList(query,Article.class).forEach(article -> lists.add(article));
        if(!CollectionUtils.isEmpty(lists)){
            return ResponseDto.getInstance(200,true,lists);
        }
        return ResponseDto.getInstance(400,false,null);
    }
}
