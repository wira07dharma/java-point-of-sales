/*
 * Activity.java
 *
 * Created on January 10, 2007, 4:08 PM
 */

package com.dimata.aiso.entity.masterdata;

/* import package qdep */
import com.dimata.qdep.entity.*;

/**
 *
 * @author  dwi
 */
public class Activity extends Entity {
    
    /**
     * Holds value of property code.
     */
    private String code = "";
    
    /**
     * Holds value of property name.
     */
    private String description = "";
    
    /**
     * Holds value of property description.
     */
    private String outPutandDelv = "";
    
    /**
     * Holds value of property idParent.
     */
    private long idParent = 0;
    
    /**
     * Holds value of property posted.
     */
    private int posted = 0;
    
    /**
     * Holds value of property type.
     */
    private int type = 0;
    
    /**
     * Holds value of property level.
     */
    private int act_level;    
    
    /**
     * Holds value of property perfm_indict.
     */
    private String perfm_indict = "";    
   
    /**
     * Holds value of property assump_and_risk.
     */
    private String assump_and_risk = "";    
    
    /**
     * Holds value of property cost_impl.
     */
    private String cost_impl = "";
    
    /**
     * Getter for property code.
     * @return Value of property code.
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getOutPutandDelv() {
        return this.outPutandDelv;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setOutPutandDelv(String outPutandDelv) {
        this.outPutandDelv = outPutandDelv;
    }
    
    /**
     * Getter for property idParent.
     * @return Value of property idParent.
     */
    public long getIdParent() {
        return this.idParent;
    }
    
    /**
     * Setter for property idParent.
     * @param idParent New value of property idParent.
     */
    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }
    
    /**
     * Getter for property posted.
     * @return Value of property posted.
     */
    public int getPosted() {
        return this.posted;
    }
    
    /**
     * Setter for property posted.
     * @param posted New value of property posted.
     */
    public void setPosted(int posted) {
        this.posted = posted;
    }
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * Getter for property level.
     * @return Value of property level.
     */
    public int getActLevel() {
        return this.act_level;
    }
    
    /**
     * Setter for property level.
     * @param level New value of property level.
     */
    public void setActLevel(int act_level) {
        this.act_level = act_level;
    }
    
    /**
     * Getter for property perfm_indict.
     * @return Value of property perfm_indict.
     */
    public String getPerfmIndict() {
        return this.perfm_indict;
    }
    
    /**
     * Setter for property perfm_indict.
     * @param perfm_indict New value of property perfm_indict.
     */
    public void setPerfmIndict(String perfm_indict) {
        this.perfm_indict = perfm_indict;
    }
    
    /**
     * Getter for property assump_and_risk.
     * @return Value of property assump_and_risk.
     */
    public String getAssumpAndRisk() {
        return this.assump_and_risk;
    }
    
    /**
     * Setter for property assump_and_risk.
     * @param assump_and_risk New value of property assump_and_risk.
     */
    public void setAssumpAndRisk(String assump_and_risk) {
        this.assump_and_risk = assump_and_risk;
    }
    
    /**
     * Getter for property cost_impl.
     * @return Value of property cost_impl.
     */
    public String getCostImpl() {
        return this.cost_impl;
    }
    
    /**
     * Setter for property cost_impl.
     * @param cost_impl New value of property cost_impl.
     */
    public void setCostImpl(String cost_impl) {
        this.cost_impl = cost_impl;
    }
    
}
