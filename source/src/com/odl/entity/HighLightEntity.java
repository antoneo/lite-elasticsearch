package com.odl.entity;

import lombok.Data;

import com.odl.model.Model;

import org.elasticsearch.common.text.Text;

/**
 * 高亮返回结果
 *
 * @author Antoneo
 * @create 2018-09-14 15:41
 **/
@Data
public class HighLightEntity  {

    /**
     * 查询实体返回
     */
    private Model<?> model;
    /**
     * 高亮部分返回
     */
    private Text[] texts;
    
}
