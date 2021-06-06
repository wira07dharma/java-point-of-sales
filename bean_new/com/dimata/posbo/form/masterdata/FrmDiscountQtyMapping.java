/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;

/**
 *
 * @author PT. Dimata
 */

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmDiscountQtyMapping extends FRMHandler implements I_FRMInterface, I_FRMType {
    private DiscountQtyMapping discountQtyMapping;

	public static final String FRM_NAME_DISCOUNTQTYMAPPING		=  "FRM_NAME_DISCOUNTQTYMAPPING" ;

	public static final int FRM_FIELD_DISCOUNT_TYPE_ID		=  0 ;
        public static final int FRM_FIELD_CURRENCY_TYPE_ID		=  1 ;
        public static final int FRM_FIELD_LOCATION_ID                   =  2 ;
	public static final int FRM_FIELD_MATERIAL_ID			=  3 ;
        public static final int FRM_FIELD_START_QTY			=  4 ;
        public static final int FRM_FIELD_TO_QTY                        =  5 ;
        public static final int FRM_FIELD_DISCOUNT_VALUE                =  6 ;
        public static final int FRM_FIELD_DISCOUNT_TYPE                 =  7 ;


	public static String[] fieldNames = {
		"FRM_FIELD_DISCOUNT_TYPE_ID",  "FRM_FIELD_CURRENCY_TYPE_ID",
                "FRM_FIELD_LOCATION_ID", "FRM_FIELD_MATERIAL_ID",
                "FRM_FIELD_START_QTY",
		"FRM_FIELD_TO_QTY",
                "FRM_FIELD_DISCOUNT_VALUE",
                "FRM_FIELD_DISCOUNT_TYPE",
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_LONG,  TYPE_LONG,
                TYPE_FLOAT, TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_INT


	} ;

	public FrmDiscountQtyMapping(){
	}
	public FrmDiscountQtyMapping(DiscountQtyMapping discountQtyMapping){
		this.discountQtyMapping = discountQtyMapping;
	}

	public FrmDiscountQtyMapping(HttpServletRequest request, DiscountQtyMapping discountQtyMapping){
		super(new FrmDiscountQtyMapping(discountQtyMapping), request);
		this.discountQtyMapping = discountQtyMapping;
	}

	public String getFormName() { return FRM_NAME_DISCOUNTQTYMAPPING; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public DiscountQtyMapping getEntityObject(){ return discountQtyMapping; }

	public void requestEntityObject(DiscountQtyMapping discountQtyMapping) {
		try{
			this.requestParam();
                        discountQtyMapping.setDiscountTypeId(getLong(FRM_FIELD_DISCOUNT_TYPE_ID));
                        discountQtyMapping.setCurrencyTypeId(getLong(FRM_FIELD_CURRENCY_TYPE_ID));
                        discountQtyMapping.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                        discountQtyMapping.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
			discountQtyMapping.setStartQty(getDouble(FRM_FIELD_START_QTY));
                        discountQtyMapping.setToQty(getDouble(FRM_FIELD_TO_QTY));
                        discountQtyMapping.setDiscountValue(getDouble(FRM_FIELD_DISCOUNT_VALUE));
                        discountQtyMapping.setDiscountType(getInt(FRM_FIELD_DISCOUNT_TYPE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
        public Vector requestEntityObject(int size, HttpServletRequest request){
            Vector vObj = new Vector();
            this.requestParam();
            for(int i=0; i<size;i++)
            try{
                        DiscountQtyMapping discQtyMapping = new DiscountQtyMapping();
                        //update opie-eyek 20130809
                        //discQtyMapping.setDiscountTypeId(getParamLong(fieldNames[FRM_FIELD_DISCOUNT_TYPE_ID]));
                         discQtyMapping.setDiscountTypeId(0);
                        discQtyMapping.setCurrencyTypeId(getParamLong(fieldNames[FRM_FIELD_CURRENCY_TYPE_ID]));
                        discQtyMapping.setLocationId(0);//getParamLong(fieldNames[FRM_FIELD_LOCATION_ID]));
                        discQtyMapping.setMaterialId(getParamLong(fieldNames[FRM_FIELD_MATERIAL_ID]));
                        
			discQtyMapping.setStartQty(getParamDouble(fieldNames[FRM_FIELD_START_QTY]+i));
                        discQtyMapping.setToQty(getParamDouble(fieldNames[FRM_FIELD_TO_QTY]+i));
                        discQtyMapping.setDiscountValue(getParamDouble(fieldNames[FRM_FIELD_DISCOUNT_VALUE]+i));
                        discQtyMapping.setDiscountType(getParamInt(fieldNames[FRM_FIELD_DISCOUNT_TYPE]+i));
                        
                        if( ( discQtyMapping.getStartQty()+ discQtyMapping.getToQty()+discQtyMapping.getDiscountValue()) > 0.0 && 
                            ( discQtyMapping.getStartQty()<=0.00 || discQtyMapping.getToQty()<=0.00 || discQtyMapping.getDiscountValue()<=0.00)){
                          this.addError(i, FRMMessage.getErr(FRMMessage.ERR_REQUIRED));
                          return new Vector();
                        }
                        if( (discQtyMapping.getStartQty()+ discQtyMapping.getToQty()+discQtyMapping.getDiscountValue()) <= 0.0)
                            continue;

                      vObj.add(discQtyMapping);
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
            return vObj;

        }

}
