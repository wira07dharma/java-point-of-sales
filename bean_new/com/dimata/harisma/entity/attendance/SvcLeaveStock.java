/*
 * SvcLeaveStock.java
 *
 * Created on February 24, 2004, 1:47 PM
 */

package com.dimata.harisma.entity.attendance;

/**
 *
 * @author  sutaya
 */
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class SvcLeaveStock extends Entity {
    
    /** Creates a new instance of SvcLeaveStock */
    private long leaveStockId;
    private Date periode;
    private int alAmount;
    private int llAmount;
    private int loss;
    
    public SvcLeaveStock() {
    }
    
    public long getLeaveStockId(){
        return leaveStockId;
    }
    public void setLeaveStockId(long leaveStockId){
        this.leaveStockId = leaveStockId;
    }
    public Date getPeriode(){
        return periode;
    }
    public void setPeriode(Date periode){
        this.periode = periode;
    }
    public int getAlAmount(){
        return alAmount;
    }
    public void setAlAmount(int alAmount){
        this.alAmount = alAmount;
    }
    public int getLlAmount(){
        return llAmount;
    }
    public void setLlAmount(int llAmount){
        this.llAmount = llAmount;;
    }
    public int getLoss(){
        return loss;
    }
    public void setLoss(int loss){
        this.loss = loss;
    }
    
    
}
