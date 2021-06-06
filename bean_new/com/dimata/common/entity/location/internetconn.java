/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.entity.location;

/* package java */
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class internetconn extends Entity implements Serializable {

   
    private String ip = "";
    private String user_name = "";
    private String pasword = "";
    private String port = "";
    private Long cash_master_id;
    private String database_name="";


    public String getPstClassName() {
        return "com.dimata.common.entity.location.PstInternet";
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the user_name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @param user_name the user_name to set
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * @return the pasword
     */
    public String getPasword() {
        return pasword;
    }

    /**
     * @param pasword the pasword to set
     */
    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the cash_master_id
     */
    public Long getCash_master_id() {
        return cash_master_id;
    }

    /**
     * @param cash_master_id the cash_master_id to set
     */
    public void setCash_master_id(Long cash_master_id) {
        this.cash_master_id = cash_master_id;
    }

    /**
     * @return the database_name
     */
    public String getDatabase_name() {
        return database_name;
    }

    /**
     * @param database_name the database_name to set
     */
    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

   
   
   
}
