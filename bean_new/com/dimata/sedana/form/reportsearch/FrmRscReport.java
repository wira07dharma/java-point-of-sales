/*
 * FrmSrcActvity.java
 *
 * Created on February 23, 2007, 9:03 AM
 */

package com.dimata.sedana.form.reportsearch;

import com.dimata.aiso.entity.search.SrcActivity;
/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.search.*;
import com.dimata.sedana.entity.reportsearch.RscReport;

/**
 *
 * @author  dwi
 */
public class FrmRscReport extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_SRC_ACTIVITY = "FRM_SRC_ACTIVITY";
    
    /**
     * Search parameter
     */
    public static final int FRM_START_DATE = 0;
    public static final int FRM_END_DATE = 1;
    public static final int FRM_NASABAH = 2;
    public static final int FRM_NO_ANGGOTA_START = 3;
    public static final int FRM_NO_ANGGOTA_END = 4;
    public static final int FRM_NO_REKENING_START = 5;
    public static final int FRM_NO_REKENING_END = 6;
    public static final int FRM_TABUNGAN = 7;
    public static final int FRM_KREDIT = 8;
    public static final int FRM_TINGKAT_KOLEKTIBILITAS = 9;
    public static final int FRM_LIST_SORT = 10;
    public static final int FRM_SUMBER_DANA = 11;
    public static final int FRM_CONTACT_TYPE = 12;
   
    public static final String[][] shortFieldNames = {
        {"Tanggal Awal","Tanggal Akhir", "Nasabah", "No Anggota Awal", "No Anggota Akhir", "No Rekening Awal", "No Rekening Akhir", "Jenis Tabungan", "Jenis Kredit", "Tingkat Kolektibilitas", "List Diurut", "Sumber Dana", "Contact Type"},
        {"Start Date","End Date", "Member", "Start Member No", "End Member No", "Start Account No", "End Account No", "Saving Type", "Jenis Kredit", "Tingkat Kolektibilitas", "List Diurut", "Sumber Dana", "Contact Type"},
    };
    
    public static final String[] fieldNames = {
        "START_DATE",
        "END_DATE",
        "NASABAH",
        "NO_ANGGOTA_START",
        "NO_ANGGOTA_END",
        "NO_REKENING_START",
        "NO_REKENING_END",
        "TABUNGAN",
        "JENIS_KREDIT",
        "TINGKAT_KOLEKTIBILITAS",
        "LIST_DIURUT",
        "SUMBER_DANA",
        "CONTACT_TYPE",
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_COLLECTION,
        TYPE_COLLECTION,
        TYPE_COLLECTION,
        TYPE_INT,
        TYPE_COLLECTION,
        TYPE_LONG
    };
    
    private RscReport rscReport = new RscReport();
  
    public static final int REPORT_TABUNGAN = 0;
    public static final int REPORT_RANGKUMAN_TABUNGAN = 1;
    public static final int REPORT_KOLEKTIBILITAS = 2;
    public static final int REPORT_RANGKUMAN_KOLEKTIBILITAS = 3;
    public static final int REPORT_TABUNGAN_PER_PINJAMAN = 4;
    
    /** Creates a new instance of FrmSrcActvity */
    public FrmRscReport() {
    }
    
    public FrmRscReport(HttpServletRequest request) {
        super(new FrmRscReport(), request);
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getFormName() {
        return this.FRM_SRC_ACTIVITY;
    }
    
    public RscReport getEntityObject(){
        return rscReport;
    }
    
    public void requestEntityObject(RscReport rscReport){
        try{
            this.requestParam();
            
            //rscReport.setTabungan(this.getVectorLong(fieldNames[FRM_TABUNGAN]));
            rscReport.setStartDate(this.getString(FRM_START_DATE));
            rscReport.setEndDate(this.getString(FRM_END_DATE));
            rscReport.setNamaNasabah(this.getString(FRM_NASABAH));
            rscReport.setNoAnggotaStart(this.getLong(FRM_NO_ANGGOTA_START));
            rscReport.setNoAnggotaEnd(this.getLong(FRM_NO_ANGGOTA_END));
            rscReport.setNoRekeningStart(this.getLong(FRM_NO_REKENING_START));
            rscReport.setNoRekeningEnd(this.getLong(FRM_NO_REKENING_END));
            
            this.rscReport = rscReport;
        }catch(Exception e){
            System.out.println("Exception on request Entity Object FrmSrcActivity ===> "+e.toString());
            rscReport = new RscReport();
        }
    }
    
    public void requestEntityObject(HttpServletRequest req){/*
        RscReport rscReport = null;
        try{
            rscReport.setTabungan(req.getParameterValues(fieldNames[FRM_TABUNGAN]));
            rscReport.setStartDate(this.getString(FRM_START_DATE));
            rscReport.setEndDate(this.getString(FRM_END_DATE));
            rscReport.setNamaNasabah(this.getString(FRM_NASABAH));
            rscReport.setNoAnggotaStart(this.getLong(FRM_NO_ANGGOTA_START));
            rscReport.setNoAnggotaEnd(this.getLong(FRM_NO_ANGGOTA_END));
            rscReport.setNoRekeningStart(this.getLong(FRM_NO_REKENING_START));
            rscReport.setNoRekeningEnd(this.getLong(FRM_NO_REKENING_END));
            
            this.rscReport = rscReport;
        } catch(Exception e) {
            System.out.println("Exception on request Entity Object FrmSrcActivity ===> "+e.toString());
            rscReport = new RscReport();
        }
        
        return rscReport;*/
    }
    
    public static void main(String[] arg){
        FrmRscReport frmRscReport = new FrmRscReport();
        System.out.println(frmRscReport.shortFieldNames.length);
        for(int i=0; i < frmRscReport.shortFieldNames.length; i++){
            
        System.out.println(frmRscReport.shortFieldNames[i][0]);
        }
    }
}
