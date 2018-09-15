package com.example.demo.dao;

import com.example.demo.Accounts;
import com.example.demo.AccountsDao;
import com.odl.entity.HighLightEntity;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Antoneo
 * @create 2018-09-14 14:29
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseDaoTest {

    @Autowired
    AccountsDao accountsDao;
    @Test
    /**
     * 插入带id
     */
    public void insert(){
        Accounts accounts=new Accounts();
        accounts.setId("77777");
        accounts.setUser("buzhidao");
        accounts.setTitle("dnqa g");
        accounts.setDesc("524140");
        Object obj=accountsDao.insert(accounts);
        System.out.println(obj.toString());
    }

    @Test
    /**
     * 插入不带id
     */
    public void insert1(){
        Accounts accounts=new Accounts();
        accounts.setUser("buzhidao");
        accounts.setTitle("dnqa g");
        accounts.setDesc("524140");
        Object obj=accountsDao.insert(accounts);
        System.out.println(obj.toString());
    }

    @Test
    /**
     * 实体id删除
     */
    public void delete(){
        insert();
        Accounts accounts=new Accounts();
        accounts.setId("77777");
        accounts.setUser("buzhidao");
        accounts.setTitle("dnqa g");
        accounts.setDesc("524140");
        Object obj=accountsDao.delete(accounts);
        System.out.println(obj.toString());
    }

    @Test
    /**
     * 实体更新
     */
    public void update(){
        insert();
        Accounts accounts=new Accounts();
        accounts.setId("77777");
        accounts.setUser("111");
        accounts.setTitle("111");
        accounts.setDesc("111");
        RestStatus rs=accountsDao.update(accounts);
        System.out.println(rs);
    }

    @Test
    /**
     * id查找
     */
    public void getById(){
        Accounts accounts=new Accounts();
        accounts.setId("77777");
        accounts.setUser("111");
        accounts.setTitle("111");
        accounts.setDesc("111");
        Object rs=accountsDao.getById(accounts);
        System.out.println(rs);
    }

    @Test
    /**
     * 搜索
     */
    public void search(){
        Accounts accounts=new Accounts();
        accounts.setTitle("工程师");
        accounts.setDesc("数据库");
        List<Accounts> rs=accountsDao.search(accounts);
        for(Accounts accounts1:rs){
            System.out.println(accounts1);
        }
    }

    @Test
    /**
     * 默认标签
     */
    public void highLight1(){
        Accounts accounts=new Accounts();
        accounts.setDesc("张申|出事");
        List<HighLightEntity> rs=accountsDao.searchHighLight(accounts,"desc");
        for(HighLightEntity entity:rs){
            System.out.println(entity);
        }
    }


    @Test
    /**
     * 自定义标签
     */
    public void highLight(){
        Accounts accounts=new Accounts();
        accounts.setDesc("张申|出事");
        List<HighLightEntity> rs=accountsDao.searchHighLight(accounts,"desc","<fonf color='red'>","</font>");
        for(HighLightEntity entity:rs){
            System.out.println(entity);
        }
    }

}
