package com.dimata.posbo.form.search;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.SrcMatConReceive;

public class FrmSrcMatConReceive extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMatConReceive srcMatConReceive;
    
    public static final String FRM_NAME_SRCMATRECEIVE    =  "FRM_NAME_SRCMATRECEIVE" ;
    
    public static final int FRM_FIELD_RECEIVESTATUS         =  0 ;
    public static final int FRM_FIELD_RECEIVENUMBER         =  1 ;
    public static final int FRM_FIELD_VENDORNAME            =  2 ;
    public static final int FRM_FIELD_RECEIVEFROMDATE       =  3 ;
    public static final int FRM_FIELD_RECEIVETODATE         =  4 ;
    public static final int FRM_FIELD_RECEIVEDATESTATUS     =  5 ;
    public static final int FRM_FIELD_RECEIVESORTBY         =  6 ;
    public static final int FRM_FIELD_RECEIVESORTBY_TYPE    =  7 ;
    public static final int FRM_FIELD_LOCATION_TYPE         =  8 ;
    public static final int FRM_FIELD_LOCATION_FROM         =  9 ;
    public static final int FRM_FIELD_LOCATION_ID           =  10 ;
    public static final int FRM_FIELD_RECEIVE_SOURCE        =  11 ;
    public static final int FRM_FIELD_INVOICE_SUPPLIER      =  12 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_RECEIVESTATUS",
        "FRM_FIELD_RECEIVENUMBER",
        "FRM_FIELD_VENDORNAME",
        "FRM_FIELD_RECEIVEFROMDATE",
        "FRM_FIELD_RECEIVETODATE",
        "FRM_FIELD_RECEIVEDATESTATUS",
        "FRM_FIELD_RECEIVESORTBY",
        "FRM_FIELD_RECEIVESORTBY_TYPE",
        "FRM_FIELD_LOCATION_TYPE",
        "FRM_FIELD_LOCATION_FROM",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_RECEIVE_SOURCE",
        "FRM_FIELD_INVOICE_SUPPLIER"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING
    } ;
    
    public FrmSrcMatConReceive(){
    }
    public FrmSrcMatConReceive(SrcMatConReceive srcMatConReceive){
        this.srcMatConReceive = srcMatConReceive;
    }
    
    public FrmSrcMatConReceive(HttpServletRequest request, SrcMatConReceive srcMatConReceive){
        super(new FrmSrcMatConReceive(srcMatConReceive), request);
        this.srcMatConReceive = srcMatConReceive;
    }
    
    public String getFormName() { return FRM_NAME_SRCMATRECEIVE; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public SrcMatConReceive getEntityObject(){ return srcMatConReceive; }
    
    public void requestEntityObject(SrcMatConReceive srcMatConReceive) {
        try {
            this.requestParam();
            srcMatConReceive.setReceivenumber(getString(FRM_FIELD_RECEIVENUMBER));
            srcMatConReceive.setVendorname(getString(FRM_FIELD_VENDORNAME));
            srcMatConReceive.setReceivefromdate(getDate(FRM_FIELD_RECEIVEFROMDATE));
            srcMatConReceive.setReceivetodate(getDate(FRM_FIELD_RECEIVETODATE));
            srcMatConReceive.setReceivedatestatus(getInt(FRM_FIELD_RECEIVEDATESTATUS));
            srcMatConReceive.setReceivesortby(getInt(FRM_FIELD_RECEIVESORTBY));
            srcMatConReceive.setReceiveSortByType(getInt(FRM_FIELD_RECEIVESORTBY_TYPE));
            srcMatConReceive.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            srcMatConReceive.setLocationFrom(getLong(FRM_FIELD_LOCATION_FROM));
            srcMatConReceive.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcMatConReceive.setReceiveSource(getInt(FRM_FIELD_RECEIVE_SOURCE));
            srcMatConReceive.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
