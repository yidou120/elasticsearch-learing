package com.edou.es;

import com.edou.es.entity.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * @ClassName EsJava
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/26 9:12
 * @Version 1.0
 */
public class EsJava {
    @Test
    public void testCreateIndex() throws UnknownHostException {
        //获取client对象
        Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
        TransportClient transportClient = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建index
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate("blog2").get();
        //释放资源
        transportClient.close();
    }

    @Test
    public void testCreateMapping() throws Exception{
        //获取client对象
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient transportClient = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造mapping
        /*{
            "mappings":{
            "article":{
                "properties":{
                    "id":{
                        "type":"long",
                                "store":true,
                                "index":"no_analyzed"
                    },
                    "title":{
                        "type":"text",
                                "store":true,
                                "index":"analyzed",
                                "analyzer":"ik_max_word"
                    },
                    "content":{
                        "type":"text",
                                "store":true,
                                "index":"analyzed",
                                "analyzer":"ik_max_word"
                    }
                }
            }
        }
        }*/
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id")
                .field("type", "long").field("store", true).field("index", false)
                .endObject()
                .startObject("title")
                .field("type", "text").field("store", true).field("index", "analyzed").field("analyzer", "ik_max_word")
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        //加入mapping
        PutMappingRequest source = Requests.putMappingRequest("blog2").type("article").source(xContentBuilder);
        transportClient.admin().indices().putMapping(source).get();
        transportClient.close();
    }

    @Test
    public void testAddDoc() throws Exception{
        //获取client
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造文档
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", 1).field("title", "test")
                .endObject();
        client.prepareIndex("blog2","article","1").setSource(xContentBuilder).get();
        //释放资源
        client.close();
    }

    @Test
    public void testAddDocByJackson() throws Exception{
        //获取client
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造doc
        Article article = new Article(11L, "test11");
        ObjectMapper objectMapper = new ObjectMapper();
        String source = objectMapper.writeValueAsString(article);
        client.prepareIndex("blog2","article",article.getId().toString()).setSource(source).get();
        //释放资源
        client.close();
    }

    @Test
    public void testTermSearch() throws Exception{
        //获取client
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造查询
        SearchResponse searchResponse = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.termQuery("title", "test")).get();
        SearchHits hits = searchResponse.getHits();
        //获取结果数据
        System.out.println("hits total:"+hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        iterator.forEachRemaining(i -> System.out.println(i.getSourceAsString()+i.getSource().get("title")));
        //释放资源
        client.close();
    }

    @Test
    public void testQueryString() throws Exception{
        //获取client
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造条件
        SearchResponse searchResponse = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.queryStringQuery("test").defaultField("title")).get();
        //获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        hits.iterator().forEachRemaining(i -> System.out.println(i.getSourceAsString()+"\n"+i.getSource().get("title")));
        //释放资源
        client.close();
    }

    @Test
    public void testSearchById() throws Exception{
        //获取client
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //构造条件
        SearchResponse searchResponse = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.idsQuery().addIds("1", "2")).get();
        //获取数据
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        hits.iterator().forEachRemaining(i -> System.out.println(i.getSourceAsString()+"\n"+i.getSource().get("id")));
        //释放资源
        client.close();
    }

    @Test
    public void testAddBatch() throws Exception{
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        for (int i = 11; i <= 100; i++) {
            Article article = new Article();
            long id = Integer.valueOf(i).longValue();
//            System.out.println(id);
            article.setId(id);
            article.setTitle(i+" 搜索工作其实很快乐");
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(article);
            client.prepareIndex("blog2", "article", String.valueOf(i)).setSource(content.getBytes(),XContentType.JSON).get();
//            client.prepareDelete("blog2","article",String.valueOf(i)).get();
        }
        client.close();
    }

    @Test
    public void testPage() throws Exception{
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse searchResponse = client.prepareSearch().setIndices("blog2").setTypes("article").setQuery(QueryBuilders.matchAllQuery()).setFrom(0).setSize(5).get();
        SearchHits hits = searchResponse.getHits();
        hits.iterator().forEachRemaining(i -> System.out.println(i.getSourceAsString()));
        client.close();
    }

    @Test
    public void testHighLight() throws Exception{
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch().setIndices("blog2").setTypes("article").setQuery(QueryBuilders.termQuery("title", "搜索"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        highlightBuilder.field("title");
        searchRequestBuilder.highlighter(highlightBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHits hits = searchResponse.getHits();
        hits.iterator().forEachRemaining(i -> {
            System.out.println(i.getSourceAsString()+"\n"+i.getHighlightFields());
            Text[] texts = i.getHighlightFields().get("title").getFragments();
            for(Text text:texts){
                System.out.println(text);
            }
        });
        client.close();
    }
}
