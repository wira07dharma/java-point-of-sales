/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.search.*;
/**
 *
 * @author dimata005
 */

public class FrmSrcPriceProtection extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    SrcPriceProtection srcPriceProtection;
    
    public static String FRM_PRICE_PROTECTION = "FRM_PRICE_PROTECTION";
    public static int FRM_FLD_FROM_DATE=0;
    public static int FRM_FLD_TO_DATE=1;
    public static int FRM_FLD_LOCATION_ID=2;
    public static int FRM_FLD_PP_NUMBER=3;
    
    public static String[] fieldNames= {
        
        "FRM_FLD_FROM_DATE",
        "FRM_FLD_TO_DATE",
        "FRM_FLD_LOCATION_ID",
        "FRM_FLD_PP_NUMBER"
    };
    public static int fieldTypes[]={
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING
    };
    
    public void requestEntity(SrcPriceProtection srcPriceProtection){
        try{
            this.requestParam();
            srcPriceProtection.setDateFrom(getDate(FRM_FLD_FROM_DATE));
            srcPriceProtection.setDateTo(getDate(FRM_FLD_TO_DATE));
            srcPriceProtection.setLocationId(getLong(FRM_FLD_LOCATION_ID));
            srcPriceProtection.setPpNumber(getString(FRM_FLD_PP_NUMBER));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of FrmPriceProtection */
    public FrmSrcPriceProtection() {
    }
    public FrmSrcPriceProtection(SrcPriceProtection srcPriceProtection) {
        this.srcPriceProtection = srcPriceProtection;
    }
    
    public FrmSrcPriceProtection(HttpServletRequest request, SrcPriceProtection srcPriceProtection) {
        super(new FrmSrcPriceProtection(srcPriceProtection),request);
        this.srcPriceProtection = srcPriceProtection;
    }
    
    public String[] getFieldNames() {
        return this.fieldNames;
        //throw new UnsupportedOperationException ();
    }
    
    public int getFieldSize() {
        return this.fieldNames.length;
        //throw new UnsupportedOperationException ();
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
        //throw new UnsupportedOperationException ();
    }
    
    public String getFormName() {
        return FRM_PRICE_PROTECTION;
        //throw new UnsupportedOperationException ();
    }
    
}