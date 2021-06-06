/*
 * SessLKU.java
 *
 * Created on December 19, 2007, 1:58 PM
 */

package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.costing.PstCosting;
import com.dimata.aiso.entity.invoice.PstInvoiceDetail;
import com.dimata.aiso.entity.invoice.PstInvoiceMain;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.LapKegiatanUsaha;
import com.dimata.aiso.entity.search.SrcLapKegiatanUsaha;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  dwi
 */
public class SessLKU {
  
  public static synchronized Vector listLapKegiatanUsaha(long lPeriodId, SrcLapKegiatanUsaha srcLapKegiatanUsaha){
    return listLapKegiatanUsaha(lPeriodId, srcLapKegiatanUsaha, 0);
  }
  
  public static synchronized Vector listLapKegiatanUsaha(long lPeriodId, SrcLapKegiatanUsaha srcLapKegiatanUsaha, int invType){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        Periode objPeriode = new Periode();
        if(lPeriodId != 0){
            try{
                objPeriode = PstPeriode.fetchExc(lPeriodId);
            }catch(Exception e){
                objPeriode = new Periode();
            }
        }
        
        try{
            String sql = " SELECT IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+" AS INVOICE "+
                     ", CT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" AS AGENT "+
                     ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+" AS NAMA "+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TERM_OF_PAYMENT]+" AS TOP "+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" AS STS "+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TAX]+" AS DEP "+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+" AS NUMBER "+
                     ", SUM(ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+" * ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_UNIT_PRICE]+") AS CREDIT "+     
                     ", SUM(ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ITEM_DISCOUNT]+") AS DISC "+
                     " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN+" AS IM "+
                     " INNER JOIN "+PstInvoiceDetail.TBL_INV_DETAIL+" AS ID "+
                     " ON IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                     " = ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+
                     " INNER JOIN "+PstContactList.TBL_CONTACT_LIST+" AS CT "+
                     " ON IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+
                     " = CT."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                     " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                     " ON ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ID_PERKIRAAN]+
                     " = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];
            
            if(srcLapKegiatanUsaha.getDateFrom() != null || srcLapKegiatanUsaha.getDateTo() != null){
                if(objPeriode != null){
                    if(objPeriode.getTglAwal().before(srcLapKegiatanUsaha.getDateFrom()) || objPeriode.getTglAwal().equals(srcLapKegiatanUsaha.getDateFrom())){
                        if(srcLapKegiatanUsaha.getDateFrom().before(srcLapKegiatanUsaha.getDateTo()) || srcLapKegiatanUsaha.getDateFrom().equals(srcLapKegiatanUsaha.getDateTo())){
                            sql += " WHERE IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+
                                    " BETWEEN '"+Formater.formatDate(srcLapKegiatanUsaha.getDateFrom(),"yyyy-MM-dd")+"' "+
                                    " AND '"+Formater.formatDate(srcLapKegiatanUsaha.getDateTo(),"yyyy-MM-dd")+"' ";
                        }
                    }
                }
            }
            
            if(invType == 0){
                sql += " AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = 0";
            }else{
                sql += " AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = 1";
            }
            
            sql += " GROUP BY IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                 ", CT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+
                     ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TERM_OF_PAYMENT]+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TAX]+
                     ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]; 
            
            sql += " ORDER BY IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER];
            
            //System.out.println("SQL SessLKU.listLapKegiatanUsaha : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            LapKegiatanUsaha objLKU = new LapKegiatanUsaha();
            ContactList objContactList = new ContactList();
            while(rs.next()){
                objLKU = new LapKegiatanUsaha();
                String strCompName = "";
                Vector vDetailCosting = new Vector(1,1);
                long oidContact = 0;
                double dCreditSales = 0.0;
                double dCashSales = 0.0;
                double dTotalCosting = 0.0;
                double dDisc = 0.0;
                long invoiceId = 0;
                double dDeposit = rs.getDouble("DEP");
                invoiceId = rs.getLong("INVOICE");
                dDisc = rs.getDouble("DISC");
                String strDes = "";
                int iTop = 0;
                iTop = rs.getInt("TOP");
                if(iTop == 0){
                    if(dDeposit > 0){
                        dCashSales = rs.getDouble("CREDIT") - dDisc + dDeposit;
                        strDes = "Deposit = "+Formater.formatNumber(dDeposit,"##,###");
                    }else{
                        dCashSales = rs.getDouble("CREDIT") - dDisc;
                    }
                    dCreditSales = 0;
                }else{
                    if(dDeposit > 0){
                        dCashSales = dDeposit;
                        dCreditSales = rs.getDouble("CREDIT") - dDeposit - dDisc;
                        strDes = "Deposit = "+Formater.formatNumber(dDeposit,"##,###");
                    }else{
                        dCashSales = 0;
                        dCreditSales = rs.getDouble("CREDIT") - dDisc;
                    }
                    
                }
                //oidContact = rs.getLong("SUPPLIER");
                if(oidContact != 0){
                    try{
                        objContactList = PstContactList.fetchExc(oidContact);
                    }catch(Exception e){objContactList = new ContactList();}
                }
                
                if(objContactList != null){
                    strCompName = objContactList.getCompName();
                }
                
                if(invoiceId != 0){
                    vDetailCosting = getCosting(invoiceId);
                    if(vDetailCosting != null && vDetailCosting.size() > 0){
                        for(int i = 0; i < vDetailCosting.size(); i++){
                            LapKegiatanUsaha oLKU = (LapKegiatanUsaha)vDetailCosting.get(i);
                            objLKU = new LapKegiatanUsaha();
                            dTotalCosting += oLKU.getCostingAmount();
                            if(i == 0){
                                if(vDetailCosting.size() == 1){
                                    objLKU.setAgent(rs.getString("AGENT"));
                                    objLKU.setInvNumber(rs.getString("NUMBER"));
                                    objLKU.setSalesAmount(dCashSales);
                                    objLKU.setCreditSalesAmount(dCreditSales);
                                    objLKU.setService(rs.getString("NAMA"));
                                    objLKU.setTermOfPayment(iTop);
                                    objLKU.setDescription(strDes);
                                    objLKU.setReference(oLKU.getReference());
                                    objLKU.setInvoiceStatus(rs.getInt("STS"));
                                    objLKU.setCostingAmount(oLKU.getCostingAmount());
                                    objLKU.setSupplier(oLKU.getSupplier());
                                    objLKU.setBalance((dCashSales + dCreditSales) - dTotalCosting);
                                    vResult.add(objLKU);
                                }else{
                                    objLKU.setAgent(rs.getString("AGENT"));
                                    objLKU.setInvNumber(rs.getString("NUMBER"));
                                    objLKU.setSalesAmount(dCashSales);
                                    objLKU.setCreditSalesAmount(dCreditSales);
                                    objLKU.setService(rs.getString("NAMA"));
                                    objLKU.setTermOfPayment(iTop);
                                    objLKU.setDescription(strDes);
                                    objLKU.setReference(oLKU.getReference());
                                    objLKU.setInvoiceStatus(rs.getInt("STS"));
                                    objLKU.setCostingAmount(oLKU.getCostingAmount());
                                    objLKU.setSupplier(oLKU.getSupplier());
                                    objLKU.setBalance(0);
                                    vResult.add(objLKU);
                                }
                            }else{
                                if(i == (vDetailCosting.size() - 1)){
                                    objLKU.setAgent("");
                                    objLKU.setCostingAmount(oLKU.getCostingAmount());
                                    objLKU.setInvNumber("");
                                    objLKU.setSalesAmount(0);
                                    objLKU.setCreditSalesAmount(0);
                                    objLKU.setService("");
                                    objLKU.setSupplier(oLKU.getSupplier());
                                    objLKU.setTermOfPayment(0);
                                    objLKU.setDescription("");
                                    objLKU.setReference(oLKU.getReference());
                                    objLKU.setInvoiceStatus(0);
                                    objLKU.setBalance((dCashSales + dCreditSales) - dTotalCosting);
                                    vResult.add(objLKU);
                                }else{
                                    objLKU.setAgent("");
                                    objLKU.setCostingAmount(oLKU.getCostingAmount());
                                    objLKU.setInvNumber("");
                                    objLKU.setSalesAmount(0);
                                    objLKU.setCreditSalesAmount(0);
                                    objLKU.setService("");
                                    objLKU.setSupplier(oLKU.getSupplier());
                                    objLKU.setTermOfPayment(0);
                                    objLKU.setDescription("");
                                    objLKU.setReference(oLKU.getReference());
                                    objLKU.setInvoiceStatus(0);
                                    objLKU.setBalance(0);
                                    vResult.add(objLKU);
                                }
                            }
                        }
                    }
                }
                //objLKU.setCosting(vDetailCosting);
                
            }
           
            rs.close();
        }catch(Exception e){
            vResult = new Vector(1,1);
        }finally{
            dbrs.close(dbrs);
        }
        return vResult;
  }
  
  public static Vector getCosting(long lInvoiceMainId){
    DBResultSet dbrs = null;
    Vector vResult = new Vector();
    try{
        String sql = " SELECT CT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" AS SUPPLIER "+
                    ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+" AS INVOICEID "+
                    ", COS."+PstCosting.fieldNames[PstCosting.FLD_REFERENCE]+" AS NO_NOTA "+
                    ", SUM (COS."+PstCosting.fieldNames[PstCosting.FLD_NUMBER]+" * COS."+PstCosting.fieldNames[PstCosting.FLD_UNIT_PRICE]+") AS COSTING "+
                    ", SUM (COS."+PstCosting.fieldNames[PstCosting.FLD_DISCOUNT]+") AS DISC "+
                    " FROM "+PstCosting.TBL_COSTING+" AS COS "+
                    " INNER JOIN "+PstContactList.TBL_CONTACT_LIST+" AS CT "+
                    " ON COS."+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+
                    " = CT."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                    " INNER JOIN "+PstInvoiceMain.TBL_AISO_INV_MAIN+" AS IM "+
                    " ON IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                    " = COS."+PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+
                    " WHERE COS."+PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+lInvoiceMainId+
                    " GROUP BY CT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+
                    ", COS."+PstCosting.fieldNames[PstCosting.FLD_REFERENCE]+
                    ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID];
       //System.out.println("SessLKU.getCosting :::: "+sql);
        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rs = dbrs.getResultSet();
        while(rs.next()){
            LapKegiatanUsaha objLKU = new LapKegiatanUsaha();
            double dCost = 0.0;
            double dDisc = 0.0;
            double dNetCost = 0.0;
            dCost = rs.getDouble("COSTING");
            dDisc = rs.getDouble("DISC");
            dNetCost = dCost - dDisc;
            objLKU.setInvoiceMainId(rs.getLong("INVOICEID"));
            objLKU.setSupplier(rs.getString("SUPPLIER"));
            objLKU.setReference(rs.getString("NO_NOTA"));
            objLKU.setCostingAmount(dNetCost);
            vResult.add(objLKU);
        }
    }catch(Exception e){}  
    return vResult;
  }
  
  public static void main(String[] arg){
        SessLKU sessLKU = new SessLKU();
        SrcLapKegiatanUsaha srcLapKegiatanUsaha = new SrcLapKegiatanUsaha();
        Date dateFr = new Date(2007 - 1900,1 - 1,1);
        Date dateTo = new Date(2007 - 1900,1 - 1,12);
        srcLapKegiatanUsaha.setDateFrom(dateFr);
        srcLapKegiatanUsaha.setDateTo(dateTo);
        Vector vResult = new Vector();
        try{
            vResult = (Vector)sessLKU.listLapKegiatanUsaha(20070801L,srcLapKegiatanUsaha);
        }catch(Exception e){ vResult = new Vector();}
        
        if(vResult != null && vResult.size() > 0){
            for(int i = 0; i < vResult.size(); i++){
                LapKegiatanUsaha objLKU = (LapKegiatanUsaha)vResult.get(i);
                System.out.println("objLKU index ke : "+i+" = "+objLKU.getSupplier());
            }
        }
  }
}
