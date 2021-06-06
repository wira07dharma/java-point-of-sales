package com.dimata.posbo.entity.masterdata;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Unit extends Entity {
    
    private String code = "";
    private String name = "";
    private long baseUnitId = 0;
    private double qtyPerBaseUnit = 0;
    
    public long getBaseUnitId(){
        return baseUnitId;
    }
    
    public void setBaseUnitId(long baseUnitId){
        this.baseUnitId = baseUnitId;
    }
    
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        if ( code == null ) {
            code = "";
        }
        this.code = code;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name) {
        if ( name == null ) {
            name = "";
        }
        this.name = name;
    }
    
    public double getQtyPerBaseUnit() {
        return qtyPerBaseUnit;
    }
    
    public void setQtyPerBaseUnit(double qtyPerBaseUnit) {
        this.qtyPerBaseUnit = qtyPerBaseUnit;
    }
    
}
