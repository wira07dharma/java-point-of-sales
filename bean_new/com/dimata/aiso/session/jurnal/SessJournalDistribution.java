/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.jurnal;

import java.util.*;
import com.dimata.aiso.entity.masterdata.BussinessCenter;
import com.dimata.aiso.entity.masterdata.PstBussinessCenter;
import com.dimata.aiso.session.report.villamanagement.*;
import com.dimata.aiso.entity.report.PstReportOwnerDistribution;
import com.dimata.aiso.entity.report.ReportOwnerDistribution;
/**
 *
 * @author D
 */
public class SessJournalDistribution {
    
    public static boolean closeJournalDistribution(long lPeriodeId){
        Vector listBussCenters = PstBussinessCenter.list(0, 1000 , "", "");
        if(listBussCenters !=null && listBussCenters.size()>0){
            for(int i=0; i< listBussCenters.size();i++){
                BussinessCenter bc = (BussinessCenter) listBussCenters.get(i);
                if (bc!=null){
                    OwnerDistributionReport own = new OwnerDistributionReport(bc.getOID(), lPeriodeId);
                    own.loadReportByAccounts(OwnDisRepAccount.clientAdvance,OwnDisRepAccount.villaRevenue,OwnDisRepAccount.managementExpense,
                                            OwnDisRepAccount.varDirectOpExpense, OwnDisRepAccount.fixDirectOpExpense,
                                            OwnDisRepAccount.indirectOpExpense,OwnDisRepAccount.otherVillaOpExpense,OwnDisRepAccount.commisionAndFee,
                                            OwnDisRepAccount.commonArea,OwnDisRepAccount.clientContribution);
                    OwnDisRepAccount vBal3Month = own.getBalance3MonthOpExp();
                    double balAdPay= vBal3Month.getThisPeriodSaldo();
                    
                    ReportOwnerDistribution rpCurr= new ReportOwnerDistribution();
                    rpCurr.setAdvance_pay_balance(balAdPay);
                    rpCurr.setBuss_center_id(bc.getOID());
                    rpCurr.setPeriod_id(lPeriodeId);                    
                    
                    try{
                        ReportOwnerDistribution rep = PstReportOwnerDistribution.fetchByPeriodId(lPeriodeId, bc.getOID());
                        if(rep!=null && (rep.getOID()>0)){
                            rep.setAdvance_pay_balance(balAdPay);
                            PstReportOwnerDistribution.updateExc(rep);
                        } else {                            
                            PstReportOwnerDistribution.insertExc(rpCurr);
                        }
                    } catch (Exception exc){
                        System.out.println(" closeJournalDistribution " +exc);
                    }
                }
            }
        }
        return true;
    }

}
