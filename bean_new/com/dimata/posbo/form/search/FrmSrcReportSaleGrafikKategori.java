package com.dimata.posbo.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.*;

public class FrmSrcReportSaleGrafikKategori extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportSaleGrafikKategori srcReportSaleGrafikKategori;

	public static final String FRM_NAME_SRCREPORTSALEGRAFIKKATEGORI    =  "FRM_NAME_SRCREPORTSALEGRAFIKKATEGORI" ;

	public static final int FRM_FIELD_LOCATION_ID	=  0 ;
	public static final int FRM_FIELD_BULAN		=  1 ;
	public static final int FRM_FIELD_TAHUN		=  2 ;
        public static final int FRM_FIELD_CATEGORY_ID	=  3 ;
        public static final int FRM_FIELD_TO_TAHUN	=  4 ;
        public static final int FRM_TRANS_TYPE	=  5 ;
        
        
	public static String[] fieldNames = 
        {
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_BULAN",
            "FRM_FIELD_TAHUN",
            "FRM_FIELD_CATEGORY_ID",
            "FRM_FIELD_TO_TAHUN",
            "FRM_TRANS_TYPE"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_INT,
            TYPE_INT,
            TYPE_LONG,
            TYPE_INT,
            TYPE_INT
	} ;

	public FrmSrcReportSaleGrafikKategori()
        {
	}
	
        public FrmSrcReportSaleGrafikKategori(SrcReportSaleGrafikKategori srcReportSaleGrafikKategori)
        {
            this.srcReportSaleGrafikKategori = srcReportSaleGrafikKategori;
	}

	public FrmSrcReportSaleGrafikKategori(HttpServletRequest request, SrcReportSaleGrafikKategori srcReportSaleGrafikKategori)
        {
            super(new FrmSrcReportSaleGrafikKategori(srcReportSaleGrafikKategori), request);
            this.srcReportSaleGrafikKategori = srcReportSaleGrafikKategori;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTSALEGRAFIKKATEGORI; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportSaleGrafikKategori getEntityObject(){ return srcReportSaleGrafikKategori; }

	public void requestEntityObject(SrcReportSaleGrafikKategori srcReportSaleGrafikKategori) 
        {
            try
            {
                this.requestParam();
                srcReportSaleGrafikKategori.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportSaleGrafikKategori.setBulan(getInt(FRM_FIELD_BULAN));
                srcReportSaleGrafikKategori.setTahun(getInt(FRM_FIELD_TAHUN));
                srcReportSaleGrafikKategori.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
                srcReportSaleGrafikKategori.setToTahun(getInt(FRM_FIELD_TO_TAHUN));
                srcReportSaleGrafikKategori.setTransType (getInt(FRM_TRANS_TYPE));
                
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
