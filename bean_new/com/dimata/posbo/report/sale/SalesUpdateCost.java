/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.sale;

import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import static com.dimata.pos.entity.billing.PstBillDetail.FLD_BILL_DETAIL_ID;
import static com.dimata.pos.entity.billing.PstBillDetail.fieldNames;
import com.dimata.posbo.db.DBHandler;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SalesUpdateCost {
    
    public static int action(Vector record) {  
       // Vector record = new Vector();
        int act=0;
        try {
            if (record != null && record.size() > 0) {
                for (int i = 0; i < record.size(); i++) {
                    Billdetail billlDetail = (Billdetail) record.get(i);
                    try{
                        int j = updateStatusItem(billlDetail.getOID(), billlDetail.getItemPriceStock(), billlDetail.getQty());
                    } catch(Exception es){
                    }
                }
            }                        
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        }
        return act;
    }
    
    public static int updateStatusItem(long oidBillDetail, double cost, double qty) {
            int ud=0;
            Date now = new Date();
            String sql = "UPDATE " + PstBillDetail.TBL_CASH_BILL_DETAIL
                    + " SET " + PstBillDetail.fieldNames[PstBillDetail.FLD_COST] + " = '" + cost + "' "
                    + " WHERE " + fieldNames[FLD_BILL_DETAIL_ID] + " = '" + oidBillDetail + "'";

            try {
                
                DBHandler.execUpdate(sql);
                
            } catch (Exception e) {
                   ud=-1;
            }
            
        return ud;
    }
}
