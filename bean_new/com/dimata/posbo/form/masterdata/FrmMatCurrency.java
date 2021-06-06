package com.dimata.posbo.form.masterdata;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMatCurrency extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private MatCurrency matCurrency;

	public static final String FRM_NAME_MAT_CURRENCY    =  "FRM_NAME_MAT_CURRENCY" ;

	public static final int FRM_FIELD_CURRENCY_ID   =  0 ;
	public static final int FRM_FIELD_CODE          =  1 ;
	public static final int FRM_FIELD_NAME          =  2 ;
	public static final int FRM_FIELD_SELLING_RATE  =  3 ;
	public static final int FRM_FIELD_BUYING_RATE   =  4 ;

	public static String[] fieldNames = 
        {
		"FRM_FIELD_CURRENCY_ID",  "FRM_FIELD_CODE",
		"FRM_FIELD_NAME", "FRM_FIELD_SELLING_RATE",
                "FRM_FIELD_BUYING_RATE"
	} ;

	public static int[] fieldTypes = 
        {
		TYPE_LONG,  TYPE_STRING  + ENTRY_REQUIRED,
		TYPE_STRING + ENTRY_REQUIRED, TYPE_FLOAT,
                TYPE_FLOAT
	} ;

	public FrmMatCurrency()
        {
	}
	
        public FrmMatCurrency(MatCurrency matCurrency)
        {
		this.matCurrency = matCurrency;
	}

	public FrmMatCurrency(HttpServletRequest request, MatCurrency matCurrency)
        {
		super(new FrmMatCurrency(matCurrency), request);
		this.matCurrency = matCurrency;
	}

	public String getFormName() { return FRM_NAME_MAT_CURRENCY; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public MatCurrency getEntityObject(){ return matCurrency; }

	public void requestEntityObject(MatCurrency matCurrency) 
        {
		try
                {
			this.requestParam();
			matCurrency.setCode(getString(FRM_FIELD_CODE));
			matCurrency.setName(getString(FRM_FIELD_NAME));
			matCurrency.setSellingRate(getDouble(FRM_FIELD_SELLING_RATE));
			matCurrency.setBuyingRate(getDouble(FRM_FIELD_BUYING_RATE));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
