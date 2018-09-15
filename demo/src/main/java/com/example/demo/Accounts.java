package com.example.demo;

import com.odl.interfaces.DocumentID;
import com.odl.interfaces.IndexName;
import com.odl.model.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 索引对应的实体类
 *
 * @author Antoneo
 * @create 2018-09-15 11:02
 **/
@Data
@IndexName(index="accounts",type="person")
public class Accounts extends Model<Accounts> implements Serializable {
    private static final long serialVersionUID = -8809789151255187301L;
    @DocumentID
    private String id;
    private String user;
    private String title;
    private String desc;
}
