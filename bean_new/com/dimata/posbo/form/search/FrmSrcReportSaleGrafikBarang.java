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

public class FrmSrcReportSaleGrafikBarang extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportSaleGrafikBarang srcReportSaleGrafikBarang;

	public static final String FRM_NAME_SRCREPORTSALEGRAFIKBARANG    =  "FRM_NAME_SRCREPORTSALEGRAFIKBARANG" ;

	public static final int FRM_FIELD_LOCATION_ID	=  0 ;
	public static final int FRM_FIELD_MATERIAL_ID	=  1 ;
	public static final int FRM_FIELD_SKU           =  2 ;
	public static final int FRM_FIELD_TAHUN		=  3 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_MATERIAL_ID",
            "FRM_FIELD_SKU",
            "FRM_FIELD_TAHUN"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_STRING,
            TYPE_INT
	} ;

	public FrmSrcReportSaleGrafikBarang()
        {
	}
	
        public FrmSrcReportSaleGrafikBarang(SrcReportSaleGrafikBarang srcReportSaleGrafikBarang)
        {
            this.srcReportSaleGrafikBarang = srcReportSaleGrafikBarang;
	}

	public FrmSrcReportSaleGrafikBarang(HttpServletRequest request, SrcReportSaleGrafikBarang srcReportSaleGrafikBarang)
        {
            super(new FrmSrcReportSaleGrafikBarang(srcReportSaleGrafikBarang), request);
            this.srcReportSaleGrafikBarang = srcReportSaleGrafikBarang;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTSALEGRAFIKBARANG; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportSaleGrafikBarang getEntityObject(){ return srcReportSaleGrafikBarang; }

	public void requestEntityObject(SrcReportSaleGrafikBarang srcReportSaleGrafikBarang) 
        {
            try
            {
                this.requestParam();
                srcReportSaleGrafikBarang.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportSaleGrafikBarang.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
                srcReportSaleGrafikBarang.setSku(getString(FRM_FIELD_SKU));
                srcReportSaleGrafikBarang.setTahun(getInt(FRM_FIELD_TAHUN));
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
