/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.purchasing;

/**
 *
 * @author dimata005
 */
import com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder;
import javax.servlet.http.*;
import com.dimata.qdep.form.*;

public class FrmDistributionPurchaseOrder extends FRMHandler implements I_FRMInterface, I_FRMType{

    private DistributionPurchaseOrder dpo;

    public static final String FRM_NAME_DISTRIBUTION_PURCHASE_ORDER = "FRM_NAME_DISTRIBUTION_PURCHASE_ORDER";
    public static final int FRM_FIELD_PURCHASE_DISTRIBUTION_ORDER_ID = 0;
    public static final int FRM_FIELD_PURCHASE_ORDER_ID = 1;
    public static final int FRM_FIELD_LOCATION_ID = 2;
    public static final int FRM_FIELD_QTY = 3;

      public static String[] fieldNames =
            {
                "FRM_FIELD_PURCHASE_DISTRIBUTION_ORDER_ID",
                "FRM_FIELD_PURCHASE_ORDER_ID",
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_QTY"
            };
    public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT
            };
    

    public FrmDistributionPurchaseOrder() {
    }

    public FrmDistributionPurchaseOrder(DistributionPurchaseOrder dpo) {
        this.dpo = dpo;
    }

    public FrmDistributionPurchaseOrder(HttpServletRequest request, DistributionPurchaseOrder dpo) {
        super(new FrmDistributionPurchaseOrder(dpo), request);
        this.dpo = dpo;
    }

    public String getFormName() {
        return FRM_NAME_DISTRIBUTION_PURCHASE_ORDER;
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

    public DistributionPurchaseOrder getEntityObject() {
        return dpo;
    }

     public void requestEntityObject(DistributionPurchaseOrder dpo) {
        try {
            this.requestParam();
            dpo.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_ORDER_ID));
            dpo.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            dpo.setQty(getDouble(FRM_FIELD_QTY));
        } catch (Exception e) {
            System.out.println("FrmOrderMaterial.requestEntityObject err : " + e.toString());
        }
    }

}
