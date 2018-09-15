package com.odl.dao;

import com.odl.entity.HighLightEntity;
import com.odl.model.Model;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es 基础DAO封装
 *
 * @author Antoneo
 * @create 2018-09-14 10:53
 **/
@Component
public abstract class BaseDao<T extends Model> {
    @Autowired
    protected TransportClient client;
    /**
     * 单记录插入操作
     * @param object
     * @return
     */
    public RestStatus insert(T object){
        IndexRequestBuilder irb=client.prepareIndex(object.getIndexName(),object.getIndexType());
        irb.setSource(ESObjectUtils.toMap(object));
        //如果实体中有DoucumentID的注解，就将其写入到索引的id里面
        if(object.hasDocumentID()){
            irb.setId(object._id());
        }
        IndexResponse ir=irb.execute().actionGet();
        return ir.status();
    }

    /**
     * 根据实体删除数据
     * @param object
     * @return
     */
    public RestStatus delete(T object){
        DeleteResponse dr=client.prepareDelete(object.getIndexName(),object.getIndexType(),object._id()).execute().actionGet();
        return dr.status();
    }

    /**
     * 根据实体修改数据
     * @param object
     * @return
     */
    public RestStatus update(T object){
        UpdateResponse ur=client.prepareUpdate(object.getIndexName(),object.getIndexType(),object._id()).setDoc(ESObjectUtils.toMap(object)).execute().actionGet();
        return ur.status();
    }


    /**
     * 通过实体的ID查询
     * @param object
     * @return
     */
    public T getById(T object){
        GetResponse gr= client.prepareGet(object.getIndexName(),object.getIndexType(),object._id()).execute().actionGet();
        //返回结果的map转实体返回
        T obj= (T) ESObjectUtils.toObject(gr.getSource(),object.getClass());
        //如果实体中有DoucumentID的注解，就将hit里记录的Document对象赋值进去
        if(obj.hasDocumentID()){
            obj.setDocumentID(gr.getId());
        }
        return obj;
    }

    /**
     * 通过实体的ID查询
     * @param index
     * @param type
     * @param id
     * @return
     */
    public Object getById(String index,String type,String id){
        GetResponse gr= client.prepareGet(index,type,id).execute().actionGet();
        return gr;
    }

    /**
     * 查询
     * @param object
     * @return
     */
    public List<T> search(T object){
        List<T> listResult=new ArrayList<>();
        //查询请求的构造器
        SearchRequestBuilder srb=client.prepareSearch(object.getIndexName()).setTypes(object.getIndexType());
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //实体转map
        Map<String,Object> map=ESObjectUtils.toMap(object);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            //只关联传入实体中有值的部分
            if(!StringUtils.isEmpty(entry.getValue())){
                //同一属性多字段查询  | 分割
                String[] strs=entry.getValue().toString().split("\\|");
                for(String str:strs) {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(entry.getKey(), str));
                }
            }
        }
        SearchResponse sr=srb.setQuery(boolQueryBuilder).execute().actionGet();
        return getReturnList(sr,object.getClass());
    }

    /**
     * 自定义QueryBuilder检索
     * @param object
     * @param queryBuilder
     * @return
     */
    public List<T> search(T object, QueryBuilder queryBuilder){
        List<T> listResult=new ArrayList<>();
        //查询请求的构造器
        SearchRequestBuilder srb=client.prepareSearch(object.getIndexName()).setTypes(object.getIndexType());
        SearchResponse sr=srb.setQuery(queryBuilder).execute().actionGet();
        return getReturnList(sr,object.getClass());
    }


    /**
     * 查询并高亮   默认的高亮（<em></em>）
     * @param object  实体
     * @param column  高亮栏
     * @return
     */
    public List<HighLightEntity> searchHighLight(T object,String column){
        List<HighLightEntity> listResult=new ArrayList<>();
        //查询请求的构造器
        SearchRequestBuilder srb=client.prepareSearch(object.getIndexName()).setTypes(object.getIndexType());
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //实体转map
        Map<String,Object> map=ESObjectUtils.toMap(object);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            //只关联传入实体中有值的部分
            if(!StringUtils.isEmpty(entry.getValue())){
                //同一属性多字段查询  | 分割
                String[] strs=entry.getValue().toString().split("\\|");
                for(String str:strs) {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(entry.getKey(), str));
                }
            }
        }
        //高亮设置
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field(column);
        //查询
        SearchResponse sr=srb.setQuery(boolQueryBuilder).highlighter(highlightBuilder).execute().actionGet();
        return getReturnList(sr,column,object.getClass());
    }

    /**
     * 查询并高亮  自定义高亮
     * 同一属性多字段查询  | 分割
     * @param object        实体
     * @param column        高亮的栏
     * @param preTags       前置标签
     * @param postTags      后置标签
     * @return
     */
    public List<HighLightEntity> searchHighLight(T object, String column, String preTags, String postTags){
        List<HighLightEntity> listResult=new ArrayList<>();
        //查询请求的构造器
        SearchRequestBuilder srb=client.prepareSearch(object.getIndexName()).setTypes(object.getIndexType());
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //实体转map
        Map<String,Object> map=ESObjectUtils.toMap(object);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            //只关联传入实体中有值的部分
            if(!StringUtils.isEmpty(entry.getValue())){
                //同一属性多字段查询  | 分割
                String[] strs=entry.getValue().toString().split("\\|");
                for(String str:strs) {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(entry.getKey(), str));
                }
            }
        }
        //设置高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field(column);
        //设置前置标签
        highlightBuilder.preTags(preTags);
        //设置后置标签
        highlightBuilder.postTags(postTags);
        //查询
        SearchResponse sr=srb.setQuery(boolQueryBuilder).highlighter(highlightBuilder).execute().actionGet();
        return getReturnList(sr,column,object.getClass());
    }

    /**
     * 自定义高亮检索
     * @param object
     * @param queryBuilder
     * @param highlightBuilder
     * @return
     */
    public List<HighLightEntity> searchHighLight(T object,String column,QueryBuilder queryBuilder,HighlightBuilder highlightBuilder){
        List<HighLightEntity> listResult=new ArrayList<>();
        //查询请求的构造器
        SearchRequestBuilder srb=client.prepareSearch(object.getIndexName()).setTypes(object.getIndexType());
        //查询
        SearchResponse sr=srb.setQuery(queryBuilder).highlighter(highlightBuilder).execute().actionGet();
        return getReturnList(sr,column,object.getClass());
    }


    /**
     * 从返回的SearchResponse中抽取出实体链表
     * @param searchResponse
     * @param clazz
     * @return
     */
    private List<T> getReturnList(SearchResponse searchResponse,Class clazz){
        List<T> listResult=new ArrayList<>();
        //遍历返回结果的Hits
        for(SearchHit hit:searchResponse.getHits()){
            //将返回的hit中的sourcemap结果转实体
            T obj= (T) ESObjectUtils.toObject(hit.getSourceAsMap(),clazz);
            //如果实体中有DoucumentID的注解，就将hit里记录的Document对象赋值进去
            if(obj.hasDocumentID()){
                obj.setDocumentID(hit.getId());
            }
            listResult.add(obj);
        }
        return listResult;
    }

    /**
     * 从返回的SearchResponse中抽取出高亮的实体链表
     * @param searchResponse
     * @param clazz
     * @return
     */
    private List<HighLightEntity> getReturnList(SearchResponse searchResponse,String column,Class clazz){
        List<HighLightEntity> listResult=new ArrayList<>();
        //遍历返回结果的Hits
        for(SearchHit hit:searchResponse.getHits()){
            HighLightEntity highLightEntity=new HighLightEntity();
            //将返回的hit中的sourcemap结果转实体
            T obj= (T) ESObjectUtils.toObject(hit.getSourceAsMap(),clazz);
            //如果实体中有DoucumentID的注解，就将hit里记录的Document对象赋值进去
            if(obj.hasDocumentID()){
                obj.setDocumentID(hit.getId());
            }
            highLightEntity.setModel(obj);
            List<String> contents=new ArrayList<>();
            Map<String,HighlightField> m=hit.getHighlightFields();
            highLightEntity.setTexts(m.get(column).getFragments());
            listResult.add(highLightEntity);
        }
        return listResult;
    }


}
