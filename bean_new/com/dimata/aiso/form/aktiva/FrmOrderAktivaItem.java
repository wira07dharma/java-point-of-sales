/* Generated by Together */

package com.dimata.aiso.form.aktiva;

import com.dimata.aiso.entity.aktiva.OrderAktivaItem;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

import javax.servlet.http.HttpServletRequest;

public class FrmOrderAktivaItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_ORDER_AKTIVA_ITEM = "FRM_ORDER_AKTIVA_ITEM";
    public static final int FRM_AKTIVA_ID = 0;
    public static final int FRM_ORDER_AKTIVA_ID = 1;
    public static final int FRM_QTY = 2;
    public static final int FRM_PRICE = 3;
    public static final int FRM_TOTAL_PRICE = 4;

    public static String[] fieldNames =
            {
                "FRM_AKTIVA_ID",
                "FRM_ORDER_AKTIVA_ID",
                "FRM_QTY",
                "FRM_PRICE",
                "FRM_TOTAL_PRICE"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_FLOAT
            };

    private OrderAktivaItem aktiva;

    public FrmOrderAktivaItem(OrderAktivaItem aktiva) {
        this.aktiva = aktiva;
    }

    public FrmOrderAktivaItem(HttpServletRequest request, OrderAktivaItem aktiva) {
        super(new FrmOrderAktivaItem(aktiva), request);
        this.aktiva = aktiva;
    }

    public String getFormName() {
        return FRM_ORDER_AKTIVA_ITEM;
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

    public OrderAktivaItem getEntityObject() {
        return aktiva;
    }

    public void requestEntityObject(OrderAktivaItem aktiva) {
        try {
            this.requestParam();
            aktiva.setAktivaId(this.getLong(FRM_AKTIVA_ID));
            aktiva.setOrderAktivaId(this.getLong(FRM_ORDER_AKTIVA_ID));
            aktiva.setQty(this.getInt(FRM_QTY));
            aktiva.setPriceOrder(this.getDouble(FRM_PRICE));
            aktiva.setTotalPriceOrder(this.getDouble(FRM_TOTAL_PRICE));

            this.aktiva = aktiva;
        } catch (Exception e) {
            aktiva = new OrderAktivaItem();
        }
    }
}
