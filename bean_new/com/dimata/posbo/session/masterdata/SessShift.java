/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 4, 2005
 * Time: 2:02:32 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.pos.entity.billing.PstBillMain;

import java.util.Vector;

public class SessShift {

    /**
     * ini untuk pengecekan data shift di penjualan
     * @param merkId
     * @return
     */
    public static boolean readyDataToDelete(long merkId) {
        boolean status = true;
        try {
            String where = PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + "=" + merkId;
            Vector vlist = PstBillMain.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessShift - readyDataToDelete : " + e.toString());
        }
        return status;
    }

}
