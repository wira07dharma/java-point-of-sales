/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.*;
/**
 *
 * @author user
 */
public class FrmConnection extends FRMHandler implements I_FRMInterface, I_FRMType{
    private OutletConnection conn;

    public static final String FRM_NAME_LOCATION = "FRM_NAME_CONNECTION";
    public static final int FRM_FIELD_OID = 0;
    public static final int FRM_FIELD_DBDRIVER = 1;
    public static final int FRM_FIELD_DBURL = 2;
    public static final int FRM_FIELD_DBUSER = 3;
    public static final int FRM_FIELD_DBPASSWD = 4;
    public static final int FRM_FIELD_DBMINCONN=5;
    public static final int FRM_FIELD_DBMAXCONN=6;
    public static final int FRM_FIELD_LOGCONN=7;
    public static final int FRM_FIELD_LOGAPP=8;
    public static final int FRM_FIELD_LOGSIZE=9;
    public static final int FRM_FIELD_FORDATE=10;
    public static final int FRM_FIELD_FORDECIMAL=11;
    public static final int FRM_FIELD_FORCURRENCY=12;
    public static final int FRM_FIELD_CASH_MASTER_ID=13;
    public static final int FRM_FIELD_TYPE_CONNECTION=14; //update opie-eyek 20130805


    public static String[] fieldNames = {
            "FRM_FIELD_OID",
            "FRM_FIELD_DBDRIVER",
            "FRM_FIELD_DBURL",
            "FRM_FIELD_DBUSER",
            "FRM_FIELD_DBPASSWD",
            "FRM_FIELD_DBMINCONN",
            "FRM_FIELD_DBMAXCONN",
            "FRM_FIELD_LOGCONN",
            "FRM_FIELD_LOGAPP",
            "FRM_FIELD_LOGSIZE",
            "FRM_FIELD_FORDATE",
            "FRM_FIELD_FORDECIMAL",
            "FRM_FIELD_FORCURRENCY",
            "FRM_FIELD_CASH_MASTER_ID",
            "FRM_FIELD_TYPE_CONNECTION"  //update opie-eyek 20130805
            
  };
        public static int[] fieldTypes = {
            TYPE_LONG,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING + ENTRY_REQUIRED,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_INT  //update opie-eyek 20130805
            
              };

    public FrmConnection() {
    }

    public FrmConnection(OutletConnection conn) {
        this.conn = conn;
    }

    public FrmConnection(HttpServletRequest request, OutletConnection conn) {
        super(new FrmConnection(conn), request);
        this.conn = conn;
    }
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public OutletConnection getEntityObject() {
        return conn;
    }

    public void requestEntityObject(OutletConnection conn) {
        try {
            this.requestParam();
            conn.setDbdriver(getString(FRM_FIELD_DBDRIVER));
            conn.setDburl(getString(FRM_FIELD_DBURL));
            conn.setDbuser(getString(FRM_FIELD_DBUSER));
            conn.setDbpasswd(getString(FRM_FIELD_DBPASSWD));
            conn.setDbminconn(getString(FRM_FIELD_DBMINCONN));
            conn.setDbmaxconn(getString(FRM_FIELD_DBMAXCONN));
            conn.setLogconn(getString(FRM_FIELD_LOGCONN));
            conn.setLogapp(getString(FRM_FIELD_LOGAPP));
            conn.setLogsize(getString(FRM_FIELD_LOGSIZE));
            conn.setFordate(getString(FRM_FIELD_FORDATE));
            conn.setFordecimal(getString(FRM_FIELD_FORDECIMAL));
            conn.setForcurrency(getString(FRM_FIELD_FORCURRENCY));
            conn.setCash_master_id(getLong(FRM_FIELD_CASH_MASTER_ID));
            conn.setTypeConnection(getInt(FRM_FIELD_TYPE_CONNECTION));
        // System.out.println("location.getParentLocationId() : " + location.getParentLocationId());
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }


}
