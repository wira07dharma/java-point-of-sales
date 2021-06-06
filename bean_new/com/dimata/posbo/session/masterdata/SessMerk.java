/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 3, 2005
 * Time: 3:57:40 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.PstMaterial;

import java.util.Vector;

public class SessMerk {

    /**
     * ini untuk pengecekan data merk
     * apakah terpakai di material
     * @param merkId
     * @return
     */
    public static boolean readyDataToDelete(long merkId) {
        boolean status = true;
        try {
            String where = PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + "=" + merkId;
            Vector vlist = PstMaterial.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessMerk - readyDataToDelete : " + e.toString());
        }
        return status;
    }
}
