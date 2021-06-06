/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 4, 2005
 * Time: 10:59:25 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.pos.session.masterCashier;

import com.dimata.pos.entity.balance.PstCashCashier;

import java.util.Vector;

public class SessMasterCashier {

    /**
     * pengecekan master cashier
     * @param unitId
     * @return
     */
    public static boolean readyDataToDelete(long mCashierId) {
        boolean status = true;
        try {
            String where = PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] + "=" + mCashierId;
            Vector vlist = PstCashCashier.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessMasterCashier - readyDataToDelete : " + e.toString());
        }
        return status;
    }
}
