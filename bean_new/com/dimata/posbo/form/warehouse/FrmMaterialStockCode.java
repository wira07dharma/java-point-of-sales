/*
 * Form Name  	:  FrmDiscountType.java
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

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;

import com.dimata.posbo.entity.warehouse.PriceProtectionItem;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class FrmMaterialStockCode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MaterialStockCode materialStockCode;
    private Vector getListPriceProtection = new Vector();
    
    public static final String FRM_NAME_MATERIAL_STOCK_CODE = "FRM_NAME_MATERIAL_STOCK_CODE";

    public static final int FRM_FIELD_MATERIAL_STOCK_CODE_ID = 0;
    public static final int FRM_FIELD_MATERIAL_ID = 1;
    public static final int FRM_FIELD_LOCATION_ID = 2;
    public static final int FRM_FIELD_STOCK_CODE = 3;
    public static final int FRM_FIELD_STOCK_STATUS = 4;

    public static String[] fieldNames = {
        "MATERIAL_STOCK_CODE_ID", "MATERIAL_ID",
        "LOCATION_ID","STOCK_CODE",
        "STOCK_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,TYPE_LONG,
        TYPE_LONG,TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT
    };

    public FrmMaterialStockCode() {
    }

    public FrmMaterialStockCode(MaterialStockCode materialStockCode) {
        this.materialStockCode = materialStockCode;
    }

    public FrmMaterialStockCode(HttpServletRequest request, MaterialStockCode materialStockCode) {
        super(new FrmMaterialStockCode(materialStockCode), request);
        this.materialStockCode = materialStockCode;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_STOCK_CODE;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public MaterialStockCode getEntityObject() {
        return materialStockCode;
    }

    public void requestEntityObject(MaterialStockCode materialStockCode) {
        try {
            this.requestParam();
            materialStockCode.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            materialStockCode.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            materialStockCode.setStockCode(getString(FRM_FIELD_STOCK_CODE));
            materialStockCode.setStockStatus(getInt(FRM_FIELD_STOCK_STATUS));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    
     public void requestEntityObjectMultiple() {
         try{
                this.requestParam();
                String inputHarga[]=null;                                
                inputHarga = this.getParamsStringValues("inputan");      

                if(inputHarga!=null && inputHarga.length>0){        
                   long supplierId=0;
                   String serialNumber="";
                   long locationId=0;
                   try{
                    for(int x=0;x<inputHarga.length;x++){
                      try{
                        serialNumber= String.valueOf(inputHarga[x].split("_")[0]);
                        supplierId = Long.parseLong(inputHarga[x].split("_")[1]);
                        locationId = Long.parseLong(inputHarga[x].split("_")[2]);
                        double amount = this.getParamDouble(serialNumber+"_"+supplierId);
                        
                        if(amount!=0){
                            PriceProtectionItem priceProtectionItem = new PriceProtectionItem();
                            priceProtectionItem.setSerialNumber(serialNumber);
                            priceProtectionItem.setAmount(amount);
                            priceProtectionItem.setSupplierId(supplierId);
                            priceProtectionItem.setLocationId(locationId);
                            
                            getListPriceProtection.add(priceProtectionItem);
                        }
                       
                      }catch(Exception exc){
                          System.out.println("exc"+exc);
                    }

                    }

                   }catch(Exception exc){
                       System.out.println("exc requestEntityObject Frm"+exc);
                   }
                }
         }catch(Exception e){
            System.out.println("Error on requestEntityObject : "+e.toString());
         }
     }

 
    
    public Vector getGetListPriceProtection() {
        return getListPriceProtection;
    }
    public int getGetListPriceProtection(Vector getListPriceProtection) {
        return getListPriceProtection.size();
    }
    
    
}
