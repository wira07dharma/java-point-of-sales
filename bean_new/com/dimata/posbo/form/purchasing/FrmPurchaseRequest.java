/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.purchasing;

import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmPurchaseRequest extends FRMHandler implements I_FRMInterface, I_FRMType {
    private PurchaseRequest pr;

    public static final String FRM_NAME_PURCHASE_REQUEST = "FRM_NAME_PURCHASE_REQUEST";
    public static final int FRM_FIELD_PURCHASE_REQUEST_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;
    public static final int FRM_FIELD_PR_CODE = 2;
    public static final int FRM_FIELD_PR_CODE_COUNTER = 3;
    public static final int FRM_FIELD_PURCH_REQUEST_DATE = 4;
    public static final int FRM_FIELD_PR_STATUS = 5;
    public static final int FRM_FIELD_REMARK = 6;
    public static final int FRM_FIELD_TERM_OF_PAYMENT = 7;
    public static final int FRM_FIELD_WAREHOUSE_SUPP_ID = 8;
    public static final int FRM_FIELD_CURRENCY_ID = 9;
    public static final int FRM_FIELD_EXCHANGE_RATE = 10;
    public static final int FRM_FIELD_BC_NUMBER = 11;
    public static final int FRM_FIELD_DOCUMENT_TYPE = 12;
    public static final int FRM_FIELD_REQUEST_SOURCE = 13;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_PURCHASE_REQUEST_ID",
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_PR_CODE",
                "FRM_FIELD_PR_CODE_COUNTER",
                "FRM_FIELD_PURCH_REQUEST_DATE",
                "FRM_FIELD_PR_STATUS",
                "FRM_FIELD_REMARK",
                "FRM_FIELD_TERM_OF_PAYMENT",
                "FRM_FIELD_WAREHOUSE_SUPP_ID",
                "FRM_FIELD_CURRENCY_ID",
                "FRM_FIELD_EXCHANGE_RATE",
                "FRM_FIELD_BC_NUMBER,",
             "FRM_FIELD_DOCUMENT_TYPE",
             "FRM_FIELD_REQUEST_SOURCE"
            };

     public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_INT
            };

    public FrmPurchaseRequest() {
    }

    public FrmPurchaseRequest(PurchaseRequest pr) {
        this.pr = pr;
    }

    public FrmPurchaseRequest(HttpServletRequest request, PurchaseRequest pr) {
        super(new FrmPurchaseRequest(pr), request);
        this.pr = pr;
    }

    public String getFormName() {
        return FRM_NAME_PURCHASE_REQUEST;
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

    public PurchaseRequest getEntityObject() {
        return pr;
    }

    public void requestEntityObject(PurchaseRequest po) {
        try {
            this.requestParam();
            String tglRequest = this.getString(FRM_FIELD_PURCH_REQUEST_DATE);
            Date data = Formater.formatDate(tglRequest, "yyyy-MM-dd");
            
            po.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            po.setPurchRequestDate(data);
            po.setPrCode(getString(FRM_FIELD_PR_CODE));
            po.setPrStatus(getInt(FRM_FIELD_PR_STATUS));
            po.setRemark(getString(FRM_FIELD_REMARK));
            po.setTermOfPayment(getInt(FRM_FIELD_TERM_OF_PAYMENT));
            po.setWarehouseSupplierId(getLong(FRM_FIELD_WAREHOUSE_SUPP_ID));
            
            po.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            po.setExhangeRate(getDouble(FRM_FIELD_EXCHANGE_RATE));
            po.setNomorBc(getString(FRM_FIELD_BC_NUMBER));
            po.setJenisDocument(getString(FRM_FIELD_DOCUMENT_TYPE));
            po.setRequestSource(getInt(FRM_FIELD_REQUEST_SOURCE));
            
        } catch (Exception e) {
            System.out.println("FrmOrderMaterial.requestEntityObject err : " + e.toString());
        }
    }

}

