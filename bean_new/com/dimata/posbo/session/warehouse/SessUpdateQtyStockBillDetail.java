/*
 * SessUpdateQtyStockBillDetail.java
 *
 * Created on February 16, 2008, 10:58 AM
 */

package com.dimata.posbo.session.warehouse;

import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import java.util.Vector;

/**
 *
 * @author gwawan
 */
public class SessUpdateQtyStockBillDetail {
    
    /** Creates a new instance of SessUpdateQtyStockBillDetail */
    public SessUpdateQtyStockBillDetail() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /**
         * Action List:
         * - get list sale item
         * - get base unit id of item from material
         * - update qty stock by base unit id
         */
        try {
            System.out.println("start to update qty stock of bill detail....");
            // get list sale item
            Vector vctBillDetail = PstBillDetail.list(0, 0, "", "");
            for(int i=0; i < vctBillDetail.size(); i++) {
                Billdetail objBilldetail = (Billdetail)vctBillDetail.get(i);
                // get cost of item from material
                Material objMaterial = PstMaterial.fetchExc(objBilldetail.getMaterialId());
                
                double qtyBase = PstUnit.getQtyPerBaseUnit(objBilldetail.getUnitId(), objMaterial.getDefaultStockUnitId());
                
                // update cost of item with material average price
                objBilldetail.setQtyStock(objBilldetail.getQty() * qtyBase);
                long oid = PstBillDetail.updateExc(objBilldetail);
                System.out.println(i+" "+objBilldetail.getQtyStock());
                if(i%100 == 0) {
                    System.out.println("sleep....");
                    Thread.sleep(1500);
                }
            }
            System.out.println("Update Success!");
        } catch(DBException dbe) {
            System.out.println(dbe.toString());
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
    
}
