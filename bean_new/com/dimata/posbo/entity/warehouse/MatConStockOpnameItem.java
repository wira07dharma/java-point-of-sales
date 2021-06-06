package com.dimata.posbo.entity.warehouse;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConStockOpnameItem extends Entity {
    
    private long stockOpnameId;
    private long materialId;
    private long unitId;
    private double qtyOpname = 0;
    private double qtySold = 0;
    private double qtySystem = 0;
    private double cost = 0.00;
    private double price = 0.00;
    
    public long getStockOpnameId(){
        return stockOpnameId;
    }
    
    public void setStockOpnameId(long matStockOpnameId){
        this.stockOpnameId = matStockOpnameId;
    }
    
    public long getMaterialId(){
        return materialId;
    }
    
    public void setMaterialId(long materialId){
        this.materialId = materialId;
    }
    
    public long getUnitId(){
        return unitId;
    }
    
    public void setUnitId(long unitId){
        this.unitId = unitId;
    }
    
    public double getQtyOpname() {
        return qtyOpname;
    }
    
    public void setQtyOpname(double qtyOpname) {
        this.qtyOpname = qtyOpname;
    }
    
    public double getQtySold() {
        return qtySold;
    }
    
    public void setQtySold(double qtySold) {
        this.qtySold = qtySold;
    }
    
    public double getQtySystem() {
        return qtySystem;
    }
    
    public void setQtySystem(double qtySystem) {
        this.qtySystem = qtySystem;
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
}
