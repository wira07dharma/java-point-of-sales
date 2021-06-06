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
public class DataSyncSql extends Entity implements Serializable {
    private Long id_data_sync;
    private String query;

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

   
}
