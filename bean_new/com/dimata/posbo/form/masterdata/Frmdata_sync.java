/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;

/**
 *
 * @author user
 */
/*
 * Form Name  	:  FrmInternet.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/



/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.common.entity.location.internetconn;
import com.dimata.posbo.entity.masterdata.*;

public class Frmdata_sync extends FRMHandler implements I_FRMInterface, I_FRMType {
    private DataSyncSql data_sync;

    public static final String FRM_NAME_LOCATION = "FRM_NAME_INTERNET";
    public static final int FRM_FIELD_OID = 0;
    public static final int FRM_FIELD_IP = 1;
    public static final int FRM_FIELD_USER_NAME = 2;
    public static final int FRM_FIELD_PASWORD = 3;
    public static final int FRM_FIELD_PORT = 4;
    public static final int FRM_CASH_MASTER_ID=5;
    public static final int FRM_DATABASE_NAME=6;



    public static String[] fieldNames = {
            "FRM_FIELD_OID",
            "FRM_FIELD_IP",
            "FRM_FIELD_USER_NAME",
            "FRM_FIELD_PASWORD",
            "FRM_FIELD_PORT",
            "FRM_FIELD_CASH_MASTER_ID",
            "FRM_FIELD_DATABASE_NAME"
    };

    public static int[] fieldTypes = {
            TYPE_LONG,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_STRING
              };

    public Frmdata_sync() {
    }

    public Frmdata_sync( DataSyncSql data_sync) {
        this.data_sync = data_sync;
    }

    public Frmdata_sync(HttpServletRequest request, DataSyncSql data_sync) {
        super(new Frmdata_sync(data_sync), request);
        this.data_sync = data_sync;
    }

    public String getFormName() {
        return FRM_NAME_LOCATION;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public DataSyncSql getEntityObject() {
        return data_sync;
    }

    public void requestEntityObject(DataSyncSql data_sync) {
        try {
            this.requestParam();
            //internet.setIp(getString(FRM_FIELD_IP));
            //internet.setUser_name(getString(FRM_FIELD_USER_NAME));
            //internet.setPasword(getString(FRM_FIELD_PASWORD));
            //internet.setPort(getString(FRM_FIELD_PORT));
            //internet.setId_location(getString(FRM_LOCATION_ID));

            //internet.setCash_master_id(getLong(FRM_CASH_MASTER_ID));
            //internet.setDatabase_name(getString(FRM_DATABASE_NAME));


           // System.out.println("internet.getParentLocationId() : " + internet.getParentLocationId());
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
        // System.out.print(internet.getIp().toString());
    }
}

