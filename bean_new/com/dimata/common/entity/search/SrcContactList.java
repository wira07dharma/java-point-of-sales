package com.dimata.common.entity.search;

import java.util.*;

public class SrcContactList {    

    private Vector type = new Vector(1,1);
    private String code = "";
    private String name = "";
    private String address = "";
    private int orderBy = 0;

    public Vector getType(){ return type; }

    public void setType(Vector type){ this.type = type; }

    public String getCode(){ return code; }

    public void setCode(String code){ this.code = code; }
    
    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }
    
    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

    public int getOrderBy(){ return orderBy; }

    public void setOrderBy(int orderBy){ this.orderBy = orderBy; }
}
