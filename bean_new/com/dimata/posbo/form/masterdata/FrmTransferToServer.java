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
public class FrmTransferToServer extends FRMHandler implements I_FRMInterface, I_FRMType{
    private TransferToServer transferServer;

    public static final String FRM_NAME_LOCATION = "FRM_TRANSFER_TO_OUTLET";
    
    public static final int FRM_FIELD_DATE_FROM = 0;
    public static final int FRM_FIELD_DATE_TO = 1;



    public static String[] fieldNames = {
            "FRM_FIELD_DATE_FROM",
            "FRM_FIELD_DATE_TO"
           

  };
        public static int[] fieldTypes = {
            TYPE_DATE,
            TYPE_DATE
        };

    public FrmTransferToServer() {
    }

    public FrmTransferToServer(TransferToServer conn) {
        this.transferServer = conn;
    }

    public FrmTransferToServer(HttpServletRequest request, TransferToServer conn) {
        super(new FrmTransferToServer(conn), request);
        this.transferServer = conn;
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

    public TransferToServer getEntityObject() {
        return transferServer;
    }

    public void requestEntityObject(TransferToServer conn) {
        try {
            this.requestParam();
            conn.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            conn.setDateTo(getDate(FRM_FIELD_DATE_TO));
         
       // System.out.println("location.getParentLocationId() : " + location.getParentLocationId());
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }


}
