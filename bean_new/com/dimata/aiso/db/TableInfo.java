/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.db;

/**
 *
 * @author Kartika
 */
public class TableInfo {
    public static final int TYPE_BASE_TABLE=0;
    public static final int TYPE_VIEW =1;
    public static final int TYPE_ERROR=2;
    public static String[] type={"BASE TABLE","VIEW","ERROR"};   
    private String tableName ="";
    private String status ="";
    private int tableType=TYPE_BASE_TABLE;

    public TableInfo(){
        
    }
    public TableInfo(String tableName,int tableType,String status ){
        this.tableName = new String(tableName);
        this.status = new String(status);
        this.tableType = tableType;
    }
    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the tableType
     */
    public int getTableType() {
        return tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(int tableType) {
        this.tableType = tableType;
    }
}
