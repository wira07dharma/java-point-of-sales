package com.dimata.common.entity.search;

import java.util.*;

public class SrcContactPerson {    

    private Vector type = new Vector(1,1);
    private String code = "";
    private String firstName = "";
    private String lastName = "";
    private String address = "";
    private int orderBy = 0;

    public Vector getType(){ return type; }

    public void setType(Vector type){ this.type = type; }

    public String getCode(){ return code; }

    public void setCode(String code){ this.code = code; }
    
    public String getFirstName(){ return firstName; }

    public void setFirstName(String firstName){ this.firstName = firstName; }
    
    public String getLastName(){ return lastName; }

    public void setLastName(String lastName){ this.lastName = lastName; }

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

    public int getOrderBy(){ return orderBy; }

    public void setOrderBy(int orderBy){ this.orderBy = orderBy; }
}
