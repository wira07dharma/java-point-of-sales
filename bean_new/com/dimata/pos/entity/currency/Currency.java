/*
 * Currency.java
 *
 * Created on June 6, 2003, 3:05 PM
 */

package com.dimata.pos.entity.currency;
import com.dimata.qdep.entity.*;
/**
 *
 * @author  Administrator
 * @version 
 */
public class Currency extends Entity {
    private long currencyId;
    private String code="";
    private String desc="";
    private double smallValue;
    
    /** Creates new Currency */
    public Currency() {
    }

    public long getCurrencyId(){
        return currencyId;}
    public void setCurrencyId(long currencyId){
        this.currencyId=currencyId;}
        
    public String getCode(){
        return code;}
    public void setCode(String code){
        this.code=code;}
        
    public String getDesc(){
        return code;}
    public void setDesc(String code){
        this.code=code;}
        
    public double getSmallValue(){
        return smallValue;}
    public void setSmallValue(double smallValue){
        this.smallValue=smallValue;}
}
