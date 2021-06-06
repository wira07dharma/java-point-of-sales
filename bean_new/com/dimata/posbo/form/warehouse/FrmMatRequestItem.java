/*
 * Form Name  	:  FrmMatRequestItem.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	:  [authorName]
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

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

public class FrmMatRequestItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatDispatchItem materialDispatchItem;
    
    public static final String FRM_NAME_MATERIALREQUESTITEM = "FRM_NAME_MATERIALREQUESTITEM" ;
    
    public static final int FRM_FIELD_MATERIAL_DISPATCH_ITEM_ID	= 0;
    public static final int FRM_FIELD_MAT_ID			= 1;
    public static final int FRM_FIELD_MAT_DISPATCH_ID		= 2;
    public static final int FRM_FIELD_REG_QUANTITY		= 3;
    public static final int FRM_FIELD_COMMENT			= 4;
    public static final int FRM_FIELD_MAT_GROUP_ID		= 5;
    
    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_DISPATCH_ITEM_ID",
        "FRM_FIELD_MAT_ID",
        "FRM_FIELD_MAT_DISPATCH_ID",
        "FRM_FIELD_REG_QUANTITY",
        "FRM_FIELD_COMMENT",
        "FRM_FIELD_MAT_GROUP_ID"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_FLOAT + ENTRY_REQUIRED,
        TYPE_STRING ,
        TYPE_LONG
    } ;
    
    public FrmMatRequestItem(){
    }
    public FrmMatRequestItem(MatDispatchItem materialDispatchItem){
        this.materialDispatchItem = materialDispatchItem;
    }
    
    public FrmMatRequestItem(HttpServletRequest request, MatDispatchItem materialDispatchItem){
        super(new FrmMatRequestItem(materialDispatchItem), request);
        this.materialDispatchItem = materialDispatchItem;
    }
    
    public String getFormName() { return FRM_NAME_MATERIALREQUESTITEM; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatDispatchItem getEntityObject(){ return materialDispatchItem; }
    
    public void requestEntityObject(MatDispatchItem materialDispatchItem) {
        try{
            this.requestParam();
            //materialDispatchItem.setMatStockId(getLong(FRM_FIELD_MAT_STOCK_ID));
            //materialDispatchItem.setMatDispatchId(getLong(FRM_FIELD_MAT_DISPATCH_ID));
            //materialDispatchItem.setRegQuantity(getInt(FRM_FIELD_REG_QUANTITY));
            //materialDispatchItem.setComment(getString(FRM_FIELD_COMMENT));
        }catch(Exception e){
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
