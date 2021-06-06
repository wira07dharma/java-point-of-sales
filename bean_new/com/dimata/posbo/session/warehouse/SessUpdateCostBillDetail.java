/*
 * SessUpdateCostBillDetail.java
 *
 * Created on February 13, 2008, 4:43 PM
 */

package com.dimata.posbo.session.warehouse;

import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import java.util.Vector;

/**
 *
 * @author gwawan
 */
public class SessUpdateCostBillDetail {
    
    /** Creates a new instance of SessUpdateCostBillDetail */
    public SessUpdateCostBillDetail() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /**
         * Action List:
         * - get list sale item
         * - get cost of item from material
         * - update cost of item with material average price
         */
        try {
            System.out.println("start to update cost of sale item....");
            // get list sale item
            Vector vctBillDetail = PstBillDetail.list(0, 0, "", "");
            for(int i=0; i < vctBillDetail.size(); i++) {
                Billdetail objBilldetail = (Billdetail)vctBillDetail.get(i);
                // get cost of item from material
                Material objMaterial = PstMaterial.fetchExc(objBilldetail.getMaterialId());
                
                // update cost of item with material average price
                objBilldetail.setCost(objMaterial.getAveragePrice());
                long oid = PstBillDetail.updateExc(objBilldetail);
            }
            System.out.println("Update Success!");
        } catch(DBException dbe) {
            System.out.println(dbe.toString());
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
    
}
