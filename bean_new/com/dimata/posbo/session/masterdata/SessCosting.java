/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.warehouse.PstMatCosting;

import java.util.Vector;

/**
 *
 * @author PT. Dimata
 */
public class SessCosting {

    public static boolean readyDataToDelete(long costingId) {
        boolean status = true;
        try {
            String where = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID] + "=" + costingId;
            Vector vlist = PstMatCosting.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessShift - readyDataToDelete : " + e.toString());
        }
        return status;
    }

}
