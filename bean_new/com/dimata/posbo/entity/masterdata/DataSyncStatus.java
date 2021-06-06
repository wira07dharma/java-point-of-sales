/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author user
 */

import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;
public class DataSyncStatus extends Entity implements Serializable {
    private Long id_data_sync_status;
    private Long id_data_sync;
    private Long id_dbconnection;
    private String  status;
    private String sql;

    /**
     * @return the id_data_sync_status
     */
    public Long getId_data_sync_status() {
        return id_data_sync_status;
    }

    /**
     * @param id_data_sync_status the id_data_sync_status to set
     */
    public void setId_data_sync_status(Long id_data_sync_status) {
        this.id_data_sync_status = id_data_sync_status;
    }

    /**
     * @return the id_data_sync
     */
    public Long getId_data_sync() {
        return id_data_sync;
    }

    /**
     * @param id_data_sync the id_data_sync to set
     */
    public void setId_data_sync(Long id_data_sync) {
        this.id_data_sync = id_data_sync;
    }

    /**
     * @return the id_dbconnection
     */
    public Long getId_dbconnection() {
        return id_dbconnection;
    }

    /**
     * @param id_dbconnection the id_dbconnection to set
     */
    public void setId_dbconnection(Long id_dbconnection) {
        this.id_dbconnection = id_dbconnection;
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
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }


    /**
     * @return the id_data_sync
     */
   

   
}
