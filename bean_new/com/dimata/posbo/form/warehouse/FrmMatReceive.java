package com.dimata.posbo.form.warehouse;

/* java package */
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.*;

public class FrmMatReceive extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MatReceive matReceive;

    public static final String FRM_NAME_MATRECEIVE = "FRM_NAME_MATRECEIVE";

    public static final int FRM_FIELD_RECEIVE_MATERIAL_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;
    public static final int FRM_FIELD_RECEIVE_FROM = 2;
    public static final int FRM_FIELD_LOCATION_TYPE = 3;
    public static final int FRM_FIELD_RECEIVE_DATE = 4;
    public static final int FRM_FIELD_REC_CODE = 5;
    public static final int FRM_FIELD_REC_CODE_CNT = 6;
    public static final int FRM_FIELD_RECEIVE_STATUS = 7;
    public static final int FRM_FIELD_RECEIVE_SOURCE = 8;
    public static final int FRM_FIELD_SUPPLIER_ID = 9;
    public static final int FRM_FIELD_PURCHASE_ORDER_ID = 10;
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID = 11;
    public static final int FRM_FIELD_RETURN_MATERIAL_ID = 12;
    public static final int FRM_FIELD_REMARK = 13;
    public static final int FRM_FIELD_INVOICE_SUPPLIER = 14;
    public static final int FRM_FIELD_TOTAL_PPN = 15;

    public static final int FRM_FIELD_REASON = 16;
    public static final int FRM_FIELD_CREDIT_TIME = 17;
    public static final int FRM_FIELD_EXPIRED_DATE = 18;
    public static final int FRM_FIELD_TERM_OF_PAYMENT = 19;
    public static final int FRM_FIELD_DISCOUNT = 20;
    public static final int FRM_FIELD_CURRENCY_ID = 21;
    public static final int FRM_FIELD_TRANS_RATE = 22;

    //include ppn
    public static final int FRM_FIELD_INCLUDE_PPN = 23;
    public static final int FRM_FIELD_RECEIVE_ITEM_TYPE = 24;
    public static final int FRM_FIELD_RECEIVE_TYPE = 25;
    public static final int FRM_FIELD_HEL = 26;
    public static final int FRM_FIELD_NILAI_TUKAR = 27;
    public static final int FRM_FIELD_BERAT = 28;
    public static final int FRM_FIELD_TOTAL_NOTA = 29;
    public static final int FRM_FIELD_KEPEMILIKAN_ID = 30;
    public static final int FRM_FIELD_BERAT24K = 31;

    public static final int FRM_FIELD_GONDOLA_ID = 32;
    public static final int FRM_FIELD_GONDOLA_TO_ID = 33;

    public static final int FRM_FIELD_TOTAL_ONGKOS = 34;
    public static final int FRM_FIELD_TOTAL_HE = 35;
	
	public static final int FRM_FIELD_JENIS_DOKUMEN = 36;
    public static final int FRM_FIELD_NOMOR_BC = 37;
	public static final int FRM_FIELD_TANGGAL_BC = 38;
	public static final int FRM_FIELD_LOCATION_PABEAN = 39;

    public static String[] fieldNames = {
        "FRM_FIELD_RECEIVE_MATERIAL_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_RECEIVE_FROM",
        "FRM_FIELD_LOCATION_TYPE",
        "FRM_FIELD_RECEIVE_DATE",
        "FRM_FIELD_REC_CODE",
        "FRM_FIELD_REC_CODE_CNT",
        "FRM_FIELD_RECEIVE_STATUS",
        "FRM_FIELD_RECEIVE_SOURCE",
        "FRM_FIELD_SUPPLIER_ID",
        "FRM_FIELD_PURCHASE_ORDER_ID",
        "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_RETURN_MATERIAL_ID",
        "FRM_FIELD_REMARK",
        "FRM_FIELD_INVOICE_SUPPLIER",
        "FRM_FIELD_TOTAL_PPN",
        "FRM_FIELD_REASON",
        "FRM_FIELD_CREDIT_TIME",
        "FRM_FIELD_EXPIRED_DATE",
        "FRM_FIELD_TERM_OF_PAYMENT",
        "FRM_FIELD_DISCOUNT",
        "FRM_FIELD_CURRENCY_ID",
        "FRM_FIELD_TRANS_RATE",
        //include ppn
        "FRM_FIELD_INCLUDE_PPN",
        "FRM_FIELD_RECEIVE_ITEM_TYPE",
        "FRM_FIELD_RECEIVE_TYPE",
        "FRM_FIELD_HEL",
        "FRM_FIELD_NILAI_TUKAR",
        "FRM_FIELD_BERAT_NOTA",
        "FRM_FIELD_TOTAL_NOTA",
        "FRM_FIELD_KEPEMILIKAN_ID",
        "FRM_FIELD_BERAT24K",
        "FRM_FIELD_GONDOLA_ID",
        "FRM_FIELD_GONDOLA_TO_ID",
        "FRM_FIELD_TOTAL_ONGKOS",
        "FRM_FIELD_TOTAL_HE",
        "FRM_FIELD_JENIS_DOKUMEN",
		"FRM_FIELD_NOMOR_BC",
		"FRM_FIELD_TANGGAL_BC",
  "FRM_FIELD_LOCATION_PABEAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
        TYPE_LONG
    };

    public FrmMatReceive() {
    }

    public FrmMatReceive(MatReceive matReceive) {
        this.matReceive = matReceive;
    }

    public FrmMatReceive(HttpServletRequest request, MatReceive matReceive) {
        super(new FrmMatReceive(matReceive), request);
        this.matReceive = matReceive;
    }

    public String getFormName() {
        return FRM_NAME_MATRECEIVE;
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

    public MatReceive getEntityObject() {
        return matReceive;
    }

    public void requestEntityObject(MatReceive matReceive) {
        try {
            this.requestParam();
            matReceive.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matReceive.setReceiveFrom(getLong(FRM_FIELD_RECEIVE_FROM));
            matReceive.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            matReceive.setReceiveDate(getDate(FRM_FIELD_RECEIVE_DATE));
            matReceive.setReceiveStatus(getInt(FRM_FIELD_RECEIVE_STATUS));
            matReceive.setReceiveSource(getInt(FRM_FIELD_RECEIVE_SOURCE));
            matReceive.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            matReceive.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_ORDER_ID));
            matReceive.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));
            matReceive.setReturnMaterialId(getLong(FRM_FIELD_RETURN_MATERIAL_ID));
            matReceive.setRemark(getString(FRM_FIELD_REMARK));
            matReceive.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
            matReceive.setTotalPpn(getDouble(FRM_FIELD_TOTAL_PPN));

            matReceive.setReason(getInt(FRM_FIELD_REASON));
            matReceive.setCreditTime(getInt(FRM_FIELD_CREDIT_TIME));
            matReceive.setTermOfPayment(getInt(FRM_FIELD_TERM_OF_PAYMENT));
            matReceive.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
            matReceive.setDiscount(getDouble(FRM_FIELD_DISCOUNT));
            matReceive.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            matReceive.setTransRate(getDouble(FRM_FIELD_TRANS_RATE));

            //include ppn
            matReceive.setIncludePpn(getInt(FRM_FIELD_INCLUDE_PPN));
            matReceive.setReceiveItemType(getInt(FRM_FIELD_RECEIVE_ITEM_TYPE));
            matReceive.setReceiveType(getInt(FRM_FIELD_RECEIVE_TYPE));
            matReceive.setHel(getDouble(FRM_FIELD_HEL));
            matReceive.setNilaiTukar(getDouble(FRM_FIELD_NILAI_TUKAR));
            matReceive.setBerat(getDouble(FRM_FIELD_BERAT));
            matReceive.setTotalNota(getDouble(FRM_FIELD_TOTAL_NOTA));
            matReceive.setKepemilikanId(getLong(FRM_FIELD_KEPEMILIKAN_ID));
            matReceive.setBerat24k(getDouble(FRM_FIELD_BERAT24K));

            matReceive.setGondolaId(getLong(FRM_FIELD_GONDOLA_ID));
            matReceive.setGondolaToId(getLong(FRM_FIELD_GONDOLA_TO_ID));

            matReceive.setTotalOngkos(getDouble(FRM_FIELD_TOTAL_ONGKOS));
            matReceive.setTotalHe(getDouble(FRM_FIELD_TOTAL_HE));
			matReceive.setJenisDokumen(getString(FRM_FIELD_JENIS_DOKUMEN));
            matReceive.setNomorBc(getString(FRM_FIELD_NOMOR_BC));
			matReceive.setTglBc(getDate(FRM_FIELD_TANGGAL_BC));
            matReceive.setLocationPabean(getLong(FRM_FIELD_LOCATION_PABEAN));
			

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
