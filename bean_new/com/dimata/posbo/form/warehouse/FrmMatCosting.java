package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.warehouse.MatCosting;

import javax.servlet.http.HttpServletRequest;


public class FrmMatCosting extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatCosting matCosting;

    public static final String FRM_NAME_COSTING_MATERIAL = "FRM_NAME_COSTING_MATERIAL";

    public static final int FRM_FIELD_COSTING_MATERIAL_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;
    public static final int FRM_FIELD_COSTING_TO = 2;
    public static final int FRM_FIELD_LOCATION_TYPE = 3;
    public static final int FRM_FIELD_COSTING_DATE = 4;
    public static final int FRM_FIELD_COSTING_CODE = 5;
    public static final int FRM_FIELD_COSTING_CODE_COUNTER = 6;
    public static final int FRM_FIELD_COSTING_STATUS = 7;
    public static final int FRM_FIELD_REMARK = 8;
    public static final int FRM_FIELD_INVOICE_SUPPLIER = 9;
    //add frm costing_id
    public static final int FRM_FIELD_COSTING_ID = 10;
    //add enable stock fisik
    public static final int FRM_FIELD_ENABLE_STOCK_FISIK = 11;
    
    public static final int FRM_FIELD_CONTACT_ID= 12;
    
    public static final int FRM_FIELD_CASH_CASHIER_ID= 13;
    
    //Ed
    public static final int FRM_FIELD_DOCUMENT_ID = 14;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_DISPATCH_MATERIAL_ID", "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_DISPATCH_TO", "FRM_FIELD_LOCATION_TYPE",
                "FRM_FIELD_DISPATCH_DATE", "FRM_FIELD_DISPATCH_CODE",
                "FRM_FIELD_DISPATCH_CODE_COUNTER", "FRM_FIELD_DF_STATUS",
                "FRM_FIELD_REMARK", "FRM_FIELD_INVOICE_SUPPLIER",
                "FRM_FIELD_COSTING_ID", "FRM_FIELD_ENABLE_STOCK_FISIK","FRM_FIELD_CONTACT_ID","FRM_FIELD_CASH_CASHIER_ID",
                "FRM_FIELD_DOCUMENT_ID"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG, TYPE_LONG + ENTRY_REQUIRED,
                TYPE_LONG, TYPE_INT,
                TYPE_DATE + ENTRY_REQUIRED, TYPE_STRING,
                TYPE_INT, TYPE_INT,
                TYPE_STRING, TYPE_STRING, TYPE_LONG, TYPE_INT,TYPE_LONG,TYPE_LONG,
                TYPE_LONG
            };

    public FrmMatCosting() {
    }

    public FrmMatCosting(MatCosting matCosting) {
        this.matCosting = matCosting;
    }

    public FrmMatCosting(HttpServletRequest request, MatCosting matCosting) {
        super(new FrmMatCosting(matCosting), request);
        this.matCosting = matCosting;
    }

    public String getFormName() {
        return FRM_NAME_COSTING_MATERIAL;
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

    public MatCosting getEntityObject() {
        return matCosting;
    }

    public void requestEntityObject(MatCosting matCosting) {
        try {
            this.requestParam();
            matCosting.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matCosting.setCostingTo(getLong(FRM_FIELD_COSTING_TO));
            matCosting.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            matCosting.setCostingDate(getDate(FRM_FIELD_COSTING_DATE));
            // matCosting.setCostingCode(getString(FRM_FIELD_COSTING_CODE));
            matCosting.setCostingStatus(getInt(FRM_FIELD_COSTING_STATUS));
            matCosting.setRemark(getString(FRM_FIELD_REMARK));
            matCosting.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
            //add colum costing id
            matCosting.setCostingId(getLong(FRM_FIELD_COSTING_ID));
            //add column enable stock fisik id
            matCosting.setEnableStockFisik(getInt(FRM_FIELD_ENABLE_STOCK_FISIK));
            
            matCosting.setContactId(getLong(FRM_FIELD_CONTACT_ID));
            matCosting.setCashCashierId(getLong(FRM_FIELD_CASH_CASHIER_ID));
            
            //Ed
            matCosting.setDocumentId(getLong(FRM_FIELD_DOCUMENT_ID));
            
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
