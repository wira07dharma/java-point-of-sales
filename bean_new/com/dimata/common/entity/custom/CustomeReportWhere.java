/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.custom;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class CustomeReportWhere  extends Entity {
    private long customeReportMasterId=0;
    private String field="";
    private int urutan=0;
    private int type=0;
    private String query="";
    private String fieldBetweenStartDate="";
    private String fieldBetweenEndDate="";
    private String keyField="";
    private String valueField="";

    /**
     * @return the customeReportMasterId
     */
    public long getCustomeReportMasterId() {
        return customeReportMasterId;
    }

    /**
     * @param customeReportMasterId the customeReportMasterId to set
     */
    public void setCustomeReportMasterId(long customeReportMasterId) {
        this.customeReportMasterId = customeReportMasterId;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the urutan
     */
    public int getUrutan() {
        return urutan;
    }

    /**
     * @param urutan the urutan to set
     */
    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the fieldBetweenStartDate
     */
    public String getFieldBetweenStartDate() {
        return fieldBetweenStartDate;
    }

    /**
     * @param fieldBetweenStartDate the fieldBetweenStartDate to set
     */
    public void setFieldBetweenStartDate(String fieldBetweenStartDate) {
        this.fieldBetweenStartDate = fieldBetweenStartDate;
    }

    /**
     * @return the fieldBetweenEndDate
     */
    public String getFieldBetweenEndDate() {
        return fieldBetweenEndDate;
    }

    /**
     * @param fieldBetweenEndDate the fieldBetweenEndDate to set
     */
    public void setFieldBetweenEndDate(String fieldBetweenEndDate) {
        this.fieldBetweenEndDate = fieldBetweenEndDate;
    }

    /**
     * @return the keyField
     */
    public String getKeyField() {
        return keyField;
    }

    /**
     * @param keyField the keyField to set
     */
    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    /**
     * @return the valueField
     */
    public String getValueField() {
        return valueField;
    }

    /**
     * @param valueField the valueField to set
     */
    public void setValueField(String valueField) {
        this.valueField = valueField;
    }
    
}
