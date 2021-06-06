/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.posbo.entity.masterdata.Voucher;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmVoucher extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Voucher entVoucher;
    public static final String FRM_NAME_VOUCHER = "FRM_NAME_VOUCHER";
    public static final int FRM_FIELD_VOUCHERID = 0;
    public static final int FRM_FIELD_VOUCHERNO = 1;
    public static final int FRM_FIELD_VOUCHERNAME = 2;
    public static final int FRM_FIELD_VOUCHERNOMINAL = 3;
    public static final int FRM_FIELD_VOUCHERTYPE = 4;
    public static final int FRM_FIELD_VOUCHERCREATEDATE = 5;
    public static final int FRM_FIELD_VOUCHERISSUEDDATE = 6;
    public static final int FRM_FIELD_VOUCHEREXPIRED = 7;
    public static final int FRM_FIELD_VOUCHEROUTLET = 8;
    public static final int FRM_FIELD_VOUCHERAUTHORIZEDNAME = 9;
    public static final int FRM_FIELD_VOUCHERAUTHORIZEDID = 10;
    public static final int FRM_FIELD_VOUCHERSTATUS = 11;
    public static final int FRM_FIELD_VOUCHERISSUEDTO = 12;
    public static String[] fieldNames = {
        "FRM_FIELD_VOUCHERID",
        "FRM_FIELD_VOUCHERNO",
        "FRM_FIELD_VOUCHERNAME",
        "FRM_FIELD_VOUCHERNOMINAL",
        "FRM_FIELD_VOUCHERTYPE",
        "FRM_FIELD_VOUCHERCREATEDATE",
        "FRM_FIELD_VOUCHERISSUEDDATE",
        "FRM_FIELD_VOUCHEREXPIRED",
        "FRM_FIELD_VOUCHEROUTLET",
        "FRM_FIELD_VOUCHERAUTHORIZEDNAME",
        "FRM_FIELD_VOUCHERAUTHORIZEDID",
        "FRM_FIELD_VOUCHERSTATUS",
        "FRM_FIELD_VOUCHERISSUEDTO"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING
    };

    public FrmVoucher() {
    }

    public FrmVoucher(Voucher entVoucher) {
        this.entVoucher = entVoucher;
    }

    public FrmVoucher(HttpServletRequest request, Voucher entVoucher) {
        super(new FrmVoucher(entVoucher), request);
        this.entVoucher = entVoucher;
    }

    public String getFormName() {
        return FRM_NAME_VOUCHER;
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

    public Voucher getEntityObject() {
        return entVoucher;
    }

    public void requestEntityObject(Voucher entVoucher) {
        try {
            this.requestParam();
            entVoucher.setVoucherNo(getString(FRM_FIELD_VOUCHERNO));
            entVoucher.setVoucherName(getString(FRM_FIELD_VOUCHERNAME));
            entVoucher.setVoucherNominal(getFloat(FRM_FIELD_VOUCHERNOMINAL));
            entVoucher.setVoucherType(getInt(FRM_FIELD_VOUCHERTYPE));
            entVoucher.setVoucherCreateDate(getDate(FRM_FIELD_VOUCHERCREATEDATE));
            entVoucher.setVoucherIssuedDate(getDate(FRM_FIELD_VOUCHERISSUEDDATE));
            entVoucher.setVoucherExpired(getDate(FRM_FIELD_VOUCHEREXPIRED));
            entVoucher.setVoucherOutlet(getLong(FRM_FIELD_VOUCHEROUTLET));
            entVoucher.setVoucherAuthorizedName(getString(FRM_FIELD_VOUCHERAUTHORIZEDNAME));
            entVoucher.setVoucherAuthorizedId(getLong(FRM_FIELD_VOUCHERAUTHORIZEDID));
            entVoucher.setVoucherStatus(getInt(FRM_FIELD_VOUCHERSTATUS));
            entVoucher.setVoucherIssuedTo(getString(FRM_FIELD_VOUCHERISSUEDTO));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}