package com.dimata.posbo.form.warehouse;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.*;

public class FrmMatDispatch extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private MatDispatch matDispatch;

	public static final String FRM_NAME_DISPATCH_MATERIAL = "FRM_NAME_DISPATCH_MATERIAL";
	public static final int FRM_FIELD_DISPATCH_MATERIAL_ID = 0;
	public static final int FRM_FIELD_LOCATION_ID = 1;
	public static final int FRM_FIELD_DISPATCH_TO = 2;
	public static final int FRM_FIELD_LOCATION_TYPE = 3;
	public static final int FRM_FIELD_DISPATCH_DATE = 4;
	public static final int FRM_FIELD_DISPATCH_CODE = 5;
	public static final int FRM_FIELD_DISPATCH_CODE_COUNTER = 6;
	public static final int FRM_FIELD_DISPATCH_STATUS = 7;
	public static final int FRM_FIELD_REMARK = 8;
	public static final int FRM_FIELD_INVOICE_SUPPLIER = 9;
	public static final int FRM_FIELD_GONDOLA_ID = 10;
	public static final int FRM_FIELD_GONDOLA_TO_ID = 11;
	public static final int FRM_FIELD_DISPATCH_ITEM_TYPE = 12;
	public static final int FRM_FIELD_JENIS_DOKUMEN = 13;
	public static final int FRM_FIELD_NOMOR_BC = 14;
	public static final int FRM_FIELD_TANGGAL_BC = 15;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_DISPATCH_MATERIAL_ID",  
			"FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_DISPATCH_TO",  
			"FRM_FIELD_LOCATION_TYPE",
            "FRM_FIELD_DISPATCH_DATE", 
			"FRM_FIELD_DISPATCH_CODE",  
            "FRM_FIELD_DISPATCH_CODE_COUNTER", 
			"FRM_FIELD_DF_STATUS",  
            "FRM_FIELD_REMARK", 
			"FRM_FIELD_INVOICE_SUPPLIER",
            "FRM_FIELD_GONDOLA_ID",
			"FRM_FIELD_GONDOLA_TO_ID",
			"FRM_FIELD_DISPATCH_ITEM_TYPE",
			"FRM_FIELD_JENIS_DOKUMEN", 
			"FRM_FIELD_NOMOR_BC", 
			"FRM_FIELD_TANGGAL_BC"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
            TYPE_LONG,  TYPE_INT,
            TYPE_DATE + ENTRY_REQUIRED, TYPE_STRING,  
            TYPE_INT,  TYPE_INT,
            TYPE_STRING, TYPE_STRING,TYPE_LONG,TYPE_LONG,TYPE_INT,
            TYPE_STRING, TYPE_STRING , TYPE_DATE
			
	} ;

	public FrmMatDispatch()
        {
	}
	
        public FrmMatDispatch(MatDispatch matDispatch)
        {
            this.matDispatch = matDispatch;
	}

	public FrmMatDispatch(HttpServletRequest request, MatDispatch matDispatch)
        {
            super(new FrmMatDispatch(matDispatch), request);
		this.matDispatch = matDispatch;
	}

	public String getFormName() { return FRM_NAME_DISPATCH_MATERIAL; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public MatDispatch getEntityObject(){ return matDispatch; }

	public void requestEntityObject(MatDispatch matDispatch) 
        {
		try
                {
                    this.requestParam();
//                    matDispatch.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
//                    matDispatch.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
//                    matDispatch.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
//                    matDispatch.setDispatchDate(getDate(FRM_FIELD_DISPATCH_DATE));
//                    matDispatch.setDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
//                    matDispatch.setDispatchStatus(getInt(FRM_FIELD_DISPATCH_STATUS));
//                    matDispatch.setRemark(getString(FRM_FIELD_REMARK));
//                    matDispatch.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
//                    matDispatch.setGondolaId(getLong(FRM_FIELD_GONDOLA_ID));
//                    matDispatch.setGondolaToId(getLong(FRM_FIELD_GONDOLA_TO_ID));
//                    matDispatch.setDispatchItemType(getInt(FRM_FIELD_DISPATCH_ITEM_TYPE));
//					matDispatch.setJenisDokumen(getString(FRM_FIELD_JENIS_DOKUMEN));
//					matDispatch.setNomorBeaCukai(getString(FRM_FIELD_NOMOR_BC));
					
					long locID = getLong(FRM_FIELD_LOCATION_ID);
					long disptachToID = getLong(FRM_FIELD_DISPATCH_TO);
					if(locID != 0){
						matDispatch.setLocationId(locID);
					}
					if(disptachToID != 0){
						matDispatch.setDispatchTo(disptachToID);
					}
					int locType = getInt(FRM_FIELD_LOCATION_TYPE);
					if(locType != 0){
						matDispatch.setLocationType(locType);
					}
					Date date = getDate(FRM_FIELD_DISPATCH_DATE);
					if(date != null){
						matDispatch.setDispatchDate(date);
					}
					String dispatchCode = getString(FRM_FIELD_DISPATCH_CODE);
					if(!dispatchCode.isEmpty()){
						matDispatch.setDispatchCode(dispatchCode);
					}
					int dispatchStatus = getInt(FRM_FIELD_DISPATCH_STATUS);
					if(dispatchStatus != 0){
						matDispatch.setDispatchStatus(dispatchStatus);
					}
					String remark = getString(FRM_FIELD_REMARK);
					if(!remark.isEmpty()){
						matDispatch.setRemark(remark);
					}
                    matDispatch.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
                    matDispatch.setGondolaId(getLong(FRM_FIELD_GONDOLA_ID));
                    matDispatch.setGondolaToId(getLong(FRM_FIELD_GONDOLA_TO_ID));
                    matDispatch.setDispatchItemType(getInt(FRM_FIELD_DISPATCH_ITEM_TYPE));
					matDispatch.setJenisDokumen(getString(FRM_FIELD_JENIS_DOKUMEN));
					matDispatch.setNomorBeaCukai(getString(FRM_FIELD_NOMOR_BC));
					matDispatch.setTanggalBC(getDate(FRM_FIELD_TANGGAL_BC)); 
		}
                catch(Exception e)
                {
                    System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
