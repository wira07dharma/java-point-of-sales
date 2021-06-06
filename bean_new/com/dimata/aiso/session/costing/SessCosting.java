/*
 * SessCosting.java
 *
 * Created on November 12, 2007, 2:41 PM
 */

package com.dimata.aiso.session.costing;

/**
 *
 * @author  dwi
 */
import com.dimata.aiso.entity.invoice.*;
import com.dimata.aiso.entity.arap.*;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.costing.Costing;
import com.dimata.aiso.entity.costing.CostingAdjustment;
import com.dimata.aiso.entity.costing.PstCosting;
import com.dimata.aiso.entity.costing.PstCostingAdjustment;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessCosting {
    
     public static final String SESS_COSTING = "SESS_COSTING"; 
     
    
     
     public static Vector listDetailCosting(long lInvoiceId, long lContactId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HPP]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HUTANG]+
                        ", SUM("+PstCosting.fieldNames[PstCosting.FLD_DISCOUNT]+
                        ") AS DISCOUNT "+
                        ", SUM("+PstCosting.fieldNames[PstCosting.FLD_NUMBER]+
                        " * "+PstCosting.fieldNames[PstCosting.FLD_UNIT_PRICE]+
                        ") AS AMOUNT FROM "+PstCosting.TBL_COSTING;
            if(lInvoiceId != 0){
                   sql += " WHERE "+PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+lInvoiceId;
                        if(lContactId != 0){
                            sql += " AND "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+" = "+lContactId;
                        }
                    sql += " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HPP]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HUTANG];
            }else{
                   sql +=  " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HPP]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]+
                        ", "+PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HUTANG];
            }
            System.out.println("SQL SessCosting.listDetailCosting :::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                double dDiscount = 0;
                double dAmount = 0;
                double dNetAmount = 0;
                dDiscount = rs.getDouble("DISCOUNT");
                dAmount = rs.getDouble("AMOUNT");
                dNetAmount = dAmount - dDiscount;
                Costing objCosting = new Costing();
                objCosting.setOID(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]));
                objCosting.setIdPerkiraanHpp(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HPP]));
                objCosting.setIdPerkiraanHutang(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_ID_PERKIRAAN_HUTANG]));
                objCosting.setContactId(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]));
                objCosting.setNetAmount(dNetAmount);
                vResult.add(objCosting);
            }
            rs.close();
            return vResult;
        }catch(Exception e){            
            System.out.println("Exception on SessCosting.listCostingDetail() ::: "+e.toString());
        }
        return new Vector(1,1);
    }
     
      public static Vector listDetailCosting(long lInvoiceId){
         return listDetailCosting(lInvoiceId, 0);
     }
      
      public static synchronized int postingJournalCosting(long bookType, long userOID, long periodeOID, InvoiceMain objInvoiceMain, long lCurrType, long lDepartmentId, Costing obCosting){
        try{                      
            JurnalUmum objJurnalUmum = new JurnalUmum();
            
            objJurnalUmum.setBookType(bookType);           
            objJurnalUmum.setContactOid(obCosting.getContactId());
            objJurnalUmum.setCurrType(lCurrType);
            objJurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            objJurnalUmum.setKeterangan("Costing for Invoice No : "+objInvoiceMain.getInvoiceNumber()+
            ", Issued Date : "+Formater.formatDate(objInvoiceMain.getIssuedDate(),"dd MMM yyyy"));
            objJurnalUmum.setPeriodeId(periodeOID);
            //String strJournalNumber = SessJurnal.generateVoucherNumber(periodeOID, objInvoiceMain.getIssuedDate());
            //objJurnalUmum.setSJurnalNumber(strJournalNumber);
            objJurnalUmum.setTglEntry(new Date());
            objJurnalUmum.setTglTransaksi(objInvoiceMain.getIssuedDate());
            objJurnalUmum.setUserId(userOID);
            //objJurnalUmum.setVoucherCounter(Integer.parseInt(strJournalNumber.substring(5)));
            //objJurnalUmum.setVoucherNo(strJournalNumber.substring(0,4));
            objJurnalUmum.setReferenceDoc(objInvoiceMain.getInvoiceNumber());    
            objJurnalUmum.setDepartmentOid(lDepartmentId);
            
            Vector vListDetail = (Vector)listDetailCosting(objInvoiceMain.getOID(), obCosting.getContactId());
            JurnalDetail objJurnalDetail = new JurnalDetail();
            Costing objCosting = new Costing();
            double dTotalAmount = 0;
            int idx = 0;
            
            System.out.println("objCosting.getNetAmount() postingJournalCosting :::: "+objCosting.getNetAmount());
            //Insert jurnal detail debet
            if(vListDetail != null && vListDetail.size() > 0){
                for(int i = 0; i < vListDetail.size(); i++){
                    objCosting = (Costing)vListDetail.get(i); 
                    objJurnalDetail = new JurnalDetail();
                    objJurnalDetail.setIndex(idx);
                    objJurnalDetail.setIdPerkiraan(objCosting.getIdPerkiraanHpp());
                    objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    objJurnalDetail.setCurrType(lCurrType);
                    objJurnalDetail.setCurrAmount(1);
                    objJurnalDetail.setDebet(objCosting.getNetAmount());
                    objJurnalDetail.setKredit(0);                    
                    objJurnalUmum.addDetails(i, objJurnalDetail);
                    idx++;
                    dTotalAmount += objCosting.getNetAmount();
                }
            }
            
            //Insert jurnal detail debet            
            objJurnalDetail = new JurnalDetail();
            objJurnalDetail.setIndex(idx);
            objJurnalDetail.setIdPerkiraan(objCosting.getIdPerkiraanHutang());            
            objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            objJurnalDetail.setCurrType(lCurrType);
            objJurnalDetail.setCurrAmount(1);
            objJurnalDetail.setDebet(0);
            objJurnalDetail.setKredit(dTotalAmount);
            objJurnalUmum.addDetails(0, objJurnalDetail);
            
            Vector vDetail = (Vector)objJurnalUmum.getJurnalDetails();
            for(int i = 0; i < vDetail.size(); i++){
                objJurnalDetail = (JurnalDetail)vDetail.get(i);
            }
            
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            try{
                ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, objJurnalUmum);
                return 1;
            }catch(Exception e){
                System.out.println("Exception on SessCosting.do posting :::: "+e.toString());
            }
        }catch(Exception e){
            System.out.println("Exception on postingJournalCosting() :::: "+e.toString());
        }
        
        return 0;
   } 
   
   public static int postJournalCosting(long bookType, long userOID, long periodeOID, InvoiceMain objInvoiceMain, long lCurrType, long lDepartmentId){
    int iJCostingResult = 0;
       try{
        Vector vListCosting = (Vector)listCosting(objInvoiceMain.getOID());
        System.out.println("vListCosting.size() journal :::: "+vListCosting.size());
        if(vListCosting != null && vListCosting.size() > 0){    
            for(int i = 0; i < vListCosting.size(); i++){
                Costing objCosting = (Costing)vListCosting.get(i);
                System.out.println("objCosting.getNetAmount() journal :::: "+objCosting.getNetAmount());                 
                iJCostingResult =  postingJournalCosting(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId, objCosting);
            }
        }
    }catch(Exception e){
        System.out.println("Exception on SessCosting.postJournalCosting() :::: "+e.toString());
    }
    
    return iJCostingResult;
   }
   
   public static synchronized int postingAccPayable(InvoiceMain objInvoiceMain, long lCurrencyId, Costing objCost){      
        try{  
                 double dAmount = 0;
                 Costing oCosting = new Costing();
                 Vector vDetail = listDetailCosting(objInvoiceMain.getOID(), objCost.getContactId());                
                 if(vDetail != null && vDetail.size() > 0){
                    for(int i=0; i < vDetail.size(); i++){
                        oCosting = (Costing)vDetail.get(i);
                        dAmount += oCosting.getNetAmount();
                    }
                 }
                 
                    ArApMain objArapMain = new ArApMain();                   
                    objArapMain.setAmount(dAmount);
                    objArapMain.setArApDocStatus(PstArApMain.STATUS_CLOSED);
                    objArapMain.setArApType(PstArApMain.TYPE_AP);
                    objArapMain.setContactId(objCost.getContactId());
                    objArapMain.setDescription("Account Payable for costing Invoice No. : "+objInvoiceMain.getInvoiceNumber());
                    objArapMain.setIdCurrency(lCurrencyId);
                    objArapMain.setIdPerkiraan(oCosting.getIdPerkiraanHutang());
                    objArapMain.setIdPerkiraanLawan(0);
                    objArapMain.setNotaDate(objInvoiceMain.getIssuedDate());
                    objArapMain.setNotaNo(objInvoiceMain.getInvoiceNumber());
                    objArapMain.setNumberOfPayment(1);
                    objArapMain.setRate(1);
                    objArapMain.setVoucherDate(new Date());
                    objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                    
                    try{
                        objArapMain = PstArApMain.createOrderNomor(objArapMain);
                        long lOid = PstArApMain.insertExc(objArapMain);                   
                    
                        if(objArapMain.getOID() != 0){
                            String strWhClause = PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_INVOICE_ID]+" = "+objInvoiceMain.getOID()+
                                                " AND "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_TYPE]+" = "+PstPaymentTerm.PAY_TERM_AP;
                            Vector vPayTerm = PstPaymentTerm.list(0,0, strWhClause, "");
                            PaymentTerm objPaymentTerm = new PaymentTerm();
                            ArApItem objArApItem = new ArApItem();
                            
                            if(vPayTerm != null && vPayTerm.size() > 0){
                                for(int p = 0; p < 1; p++){
                                    objPaymentTerm = (PaymentTerm)vPayTerm.get(p);
                                    objArApItem.setAngsuran(dAmount);
                                    objArApItem.setArApMainId(objArapMain.getOID());
                                    objArApItem.setArApItemStatus(0);
                                    objArApItem.setCurrencyId(lCurrencyId);
                                    objArApItem.setDescription(objPaymentTerm.getNote());
                                    objArApItem.setDueDate(objPaymentTerm.getPlanDate());
                                    objArApItem.setLeftToPay(dAmount);
                                    objArApItem.setRate(1);
                                    objArApItem.setReceiveAktivaId(0);
                                    objArApItem.setSellingAktivaId(0);                                    
                                   
                                    try{
                                        long longOID = PstArApItem.insertExc(objArApItem);
                                        if(objArApItem.getOID() != 0){
                                            try{
                                               if(vDetail != null && vDetail.size() > 0){
                                                   for(int i = 0; i < vDetail.size(); i++){
                                                       Costing tempObjCosting = (Costing)vDetail.get(i);
                                                       if(tempObjCosting.getOID() != 0){
                                                            Costing objCosting = PstCosting.fetchExc(tempObjCosting.getOID());
                                                            objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                                            long lUpdate = PstCosting.updateExc(objCosting);
                                                       }
                                                   }
                                               }
                                               objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                               objArapMain.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                               long lUpdateArapMain = PstArApMain.updateExc(objArapMain);
                                               return 1;
                                            }catch(Exception e){
                                                System.out.println("Exception on update costing status ::: "+e.toString());
                                            }
                                        }
                                    }catch(Exception e){
                                        System.out.println("Exception on SessInvoice.insertArApItem :::: "+e.toString());
                                    }     
                                }
                            }
                        }
                        }catch(Exception e){
                        System.out.println("Exception on SessInvoice.insertAPMain :::: "+e.toString());
                    }
            
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingAccPayable() ::: "+e.toString());
        }
      return 0;  
   }
   
   public static synchronized int postAccPayable(InvoiceMain objInvoiceMain, long lCurrencyId){
    int iResult = 0;   
    try{
        Vector vListCosting = (Vector)listCosting(objInvoiceMain.getOID());
        System.out.println("vListCosting.size() payable :::: "+vListCosting.size());
        Costing objCosting = new Costing();
        if(vListCosting != null && vListCosting.size() > 0){
            for(int i = 0; i < vListCosting.size(); i++){
                objCosting = (Costing)vListCosting.get(i);
                System.out.println("objCosting.getNetAmount() payable :::: "+objCosting.getNetAmount());
                iResult =  postingAccPayable(objInvoiceMain, lCurrencyId,objCosting);
            }
        }
    }catch(Exception e){
        System.out.println("Exception on SessCosting.postAccPayable() ::: "+e.toString());
    }
    return iResult;
   }
   
    public void postingAdjAccPayable(long lJurnalUmumId, CostingAdjustment objAdjCosting, long lCurrencyId){      
        try{
             if(lJurnalUmumId != 0 && objAdjCosting.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                 double dAmount = 0;                 
                 Vector vDetail = listCostingAdjDetail(objAdjCosting.getCostingId());
                 CostingAdjustment objCostingAdj = new CostingAdjustment();
                 if(vDetail != null && vDetail.size() > 0){
                    for(int i=0; i < vDetail.size(); i++){
                        CostingAdjustment oCostingAdj = (CostingAdjustment)vDetail.get(i);
                        dAmount += oCostingAdj.getNetAmount();
                    }
                 }
                    Costing objCosting = PstCosting.fetchExc(objAdjCosting.getCostingId());
                    InvoiceMain objInvoiceMain = PstInvoiceMain.fetchExc(objCosting.getInvoiceId());
                    ArApMain objArapMain = new ArApMain();                   
                    objArapMain.setAmount(dAmount);
                    objArapMain.setArApDocStatus(PstArApMain.STATUS_CLOSED);
                    objArapMain.setArApType(PstArApMain.TYPE_AP);
                    objArapMain.setContactId(objAdjCosting.getContactId());
                    objArapMain.setDescription("Adjustment Costing for Invoice No. : "+objInvoiceMain.getInvoiceNumber()+
                    ", Issued Date : "+Formater.formatDate(objInvoiceMain.getIssuedDate(),"dd MMM yyyy"));
                    objArapMain.setIdCurrency(lCurrencyId);
                    objArapMain.setIdPerkiraan(objCosting.getIdPerkiraanHutang());
                    objArapMain.setIdPerkiraanLawan(0);
                    objArapMain.setNotaDate(objInvoiceMain.getIssuedDate());
                    objArapMain.setNotaNo(objInvoiceMain.getInvoiceNumber());
                    objArapMain.setNumberOfPayment(1);
                    objArapMain.setRate(1);
                    objArapMain.setVoucherDate(new Date());
                    objArapMain.setArApMainStatus(0);
                    
                    try{
                        objArapMain = PstArApMain.createOrderNomor(objArapMain);
                        long lOid = PstArApMain.insertExc(objArapMain);
                    }catch(Exception e){
                        System.out.println("Exception on SessInvoice.insertARAdjMain :::: "+e.toString());
                    }
                    
                    if(objArapMain.getOID() != 0){
                        String strWhClause = PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_INVOICE_ID]+" = "+objAdjCosting.getCostingId()+
                                            " AND "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_TYPE]+" = "+PstPaymentTerm.PAY_TERM_AP;
                        Vector vPayTerm = PstPaymentTerm.list(0,0, strWhClause, "");
                        PaymentTerm objPaymentTerm = new PaymentTerm();
                        ArApItem objArApItem = new ArApItem();
                        if(vPayTerm != null && vPayTerm.size() > 0){
                            for(int i = 0; i < vPayTerm.size(); i++){
                                objPaymentTerm = (PaymentTerm)vPayTerm.get(i);
                                objArApItem.setAngsuran(dAmount);
                                objArApItem.setArApMainId(objArapMain.getOID());
                                objArApItem.setArApItemStatus(0);
                                objArApItem.setCurrencyId(lCurrencyId);
                                objArApItem.setDescription("Adjustment Costing for Invoice No."+objInvoiceMain.getInvoiceNumber());
                                objArApItem.setDueDate(objPaymentTerm.getPlanDate());
                                objArApItem.setLeftToPay(0);
                                objArApItem.setRate(1);
                                objArApItem.setReceiveAktivaId(0);
                                objArApItem.setSellingAktivaId(0);
                            }
                        }
                        try{
                            long lOid = PstArApItem.insertExc(objArApItem);
                            if(objArApItem.getOID() != 0){
                                objAdjCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_POSTED); 
                                long lUpdate = PstCostingAdjustment.updateExc(objAdjCosting);
                                objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                objArapMain.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                long lUpdateArapMain = PstArApMain.updateExc(objArapMain);
                            }
                        }catch(Exception e){
                            System.out.println("Exception on SessCosting.insertAdjCostingToArapItem :::: "+e.toString());
                        }     
                    }                       
             }
        }catch(Exception e){
            System.out.println("Exception on SessCosting.postingAdjAccPayable() ::: "+e.toString());
        }       
   }
    
    public void postingPaymentTerm(long lCostingId){
        try{
            Costing objCosting = new Costing();
            try{
                objCosting = PstCosting.fetchExc(lCostingId);
            }catch(Exception e){
                System.out.println("Exception on fetch ObjCosting ::: "+e.toString());
            }
            
            double dAmount = 0;
            Vector vDetail = (Vector)listDetailCosting(objCosting.getInvoiceId());
            if(vDetail != null && vDetail.size() > 0){
                for(int i = 0; i < vDetail.size(); i++){
                    Costing oCosting = (Costing)vDetail.get(i);
                    dAmount = oCosting.getNetAmount();
                }
            }
            
            InvoiceMain objInvoiceMain = PstInvoiceMain.fetchExc(objCosting.getInvoiceId());
            Date issuedDate = objInvoiceMain.getIssuedDate();
            int iDate = issuedDate.getDate();
            int iDueDate = iDate + objInvoiceMain.getTermOfPayment();
            
            Date dueDate = (Date)issuedDate.clone();
            dueDate.setDate(iDueDate);
            
            PaymentTerm objPaymentTerm = new PaymentTerm();
            objPaymentTerm.setAmount(dAmount);
            objPaymentTerm.setInvoiceId(lCostingId);
            objPaymentTerm.setNote("Plan payment costing for invoice no : "+objInvoiceMain.getInvoiceNumber());
            objPaymentTerm.setPlanDate(dueDate);
            objPaymentTerm.setType(PstPaymentTerm.PAY_TERM_AP);
            
            try{
                long lOid = PstPaymentTerm.insertExc(objPaymentTerm);
            }catch(Exception e){
                System.out.println("Exception on SessCosting.InsertObjPaymentTerm :::: "+e.toString());
            }
        }catch(Exception e){
            System.out.println("Exception on SessCosting.postingPaymentTerm() ::: "+e.toString());
        }
    }
    
    public void postingJurnalCostingAdj(long bookType, long userOID, long periodeOID, CostingAdjustment objCostingAdj, long lCurrType, long lDepartmentId){
        try{            
            Costing objCosting = new Costing();
            try{
            objCosting = PstCosting.fetchExc(objCostingAdj.getCostingId());
            }catch(Exception e){}
            InvoiceMain objInvoiceMain = new InvoiceMain();
            try{
            objInvoiceMain = PstInvoiceMain.fetchExc(objCosting.getInvoiceId());
            }catch(Exception e){}
            JurnalUmum objJurnalUmum = new JurnalUmum();
            
            objJurnalUmum.setBookType(bookType);
            objJurnalUmum.setContactOid(objCostingAdj.getContactId());
            objJurnalUmum.setCurrType(lCurrType);
            objJurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            objJurnalUmum.setKeterangan("Adjustment Costing for invoice no : "+objInvoiceMain.getInvoiceNumber()+
            ", Issued Date : "+Formater.formatDate(objInvoiceMain.getIssuedDate(),"dd MMM yyyy"));
            objJurnalUmum.setPeriodeId(periodeOID);
            //String strJournalNumber = SessJurnal.generateVoucherNumber(periodeOID, objInvoiceMain.getIssuedDate());
            //objJurnalUmum.setSJurnalNumber(strJournalNumber);
            objJurnalUmum.setTglEntry(new Date());
            objJurnalUmum.setTglTransaksi(objInvoiceMain.getIssuedDate());
            objJurnalUmum.setUserId(userOID);
            //objJurnalUmum.setVoucherCounter(Integer.parseInt(strJournalNumber.substring(5)));
            //objJurnalUmum.setVoucherNo(strJournalNumber.substring(0,4));
            objJurnalUmum.setReferenceDoc(objInvoiceMain.getInvoiceNumber());
            objJurnalUmum.setDepartmentOid(lDepartmentId);
            
            double dAmount = 0;
            double dAmountMinus = 0;
            double dAmountPlus = 0;
            int idx = 0;
            Vector vDetail = (Vector)listCostingAdjDetail(objCostingAdj.getCostingId());  
            JurnalDetail objJurnalDetail = new JurnalDetail();
            for(int i = 0; i < vDetail.size(); i++){
                CostingAdjustment oCostingAdj = (CostingAdjustment)vDetail.get(i);
                dAmount = objCostingAdj.getNetAmount();
                if(dAmount < 0){ 
                    objJurnalDetail = new JurnalDetail();
                    objJurnalDetail.setIndex(idx);
                    objJurnalDetail.setIdPerkiraan(objCosting.getIdPerkiraanHpp());
                    objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    objJurnalDetail.setCurrType(lCurrType);
                    objJurnalDetail.setCurrAmount(1);
                    objJurnalDetail.setDebet(0);
                    objJurnalDetail.setKredit(oCostingAdj.getNetAmount() * -1);
                    objJurnalUmum.addDetails(idx, objJurnalDetail);
                    dAmountMinus += oCostingAdj.getNetAmount();
                    idx++;                    
                }else{
                    objJurnalDetail = new JurnalDetail();
                    objJurnalDetail.setIndex(idx);
                    objJurnalDetail.setIdPerkiraan(objCosting.getIdPerkiraanHpp());
                    objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    objJurnalDetail.setCurrType(lCurrType);
                    objJurnalDetail.setCurrAmount(1);
                    objJurnalDetail.setDebet(oCostingAdj.getNetAmount());
                    objJurnalDetail.setKredit(0);
                    objJurnalUmum.addDetails(idx, objJurnalDetail);
                    dAmountPlus += oCostingAdj.getNetAmount();
                    idx++;           
                }
            }
            
            double dTotHutang = dAmountPlus + dAmountMinus;
            objJurnalDetail = new JurnalDetail();
            objJurnalDetail.setIndex(0);
            objJurnalDetail.setIdPerkiraan(objCosting.getIdPerkiraanHutang());
            objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            objJurnalDetail.setCurrType(lCurrType);
            objJurnalDetail.setCurrAmount(1);
            if(dTotHutang < 0){
                objJurnalDetail.setDebet(dTotHutang * -1);
                objJurnalDetail.setKredit(0);
            }else{
                objJurnalDetail.setDebet(0);
                objJurnalDetail.setKredit(dTotHutang);
            }
            objJurnalUmum.addDetails(idx, objJurnalDetail);
            
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            try{
                ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, objJurnalUmum);
            }catch(Exception e){System.out.println("Exception on posting journal :: "+e.toString());}
            try{
                objCostingAdj.setStatus(I_DocStatus.DOCUMENT_STATUS_FINAL); 
                long lOid = PstCostingAdjustment.updateExc(objCostingAdj);
            }catch(Exception e){
                System.out.println("Exception on update Costing Adjustment ::::: "+e.toString());
            }
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingJurnalCostingAdjustment() ::: "+e.toString());
        }
    }
    
    
    
    private static Vector listCosting(long lInvoiceMainId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]+
                        " FROM "+PstCosting.TBL_COSTING;
            if(lInvoiceMainId != 0){
                   sql += " WHERE "+PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+lInvoiceMainId+
                        " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID];
            }else{
                   sql +=  " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID];
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){               
                Costing objCosting = new Costing();               
                objCosting.setContactId(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID]));                
                vResult.add(objCosting);
            }
            rs.close();
            return vResult;
        }catch(Exception e){            
            System.out.println("Exception on SessInvoice.listCosting() ::: "+e.toString());
        }
        return new Vector(1,1);
    }
    
    private Vector listCostingAdjDetail(long lCostingId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT "+PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_CONTACT_ID]+
                        ", SUM("+PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_DISCOUNT]+
                        ") AS DISCOUNT "+
                        ", SUM("+PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_NUMBER]+
                        " * "+PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_UNIT_PRICE]+
                        ") AS AMOUNT FROM "+PstCostingAdjustment.TBL_COSTING_ADJ;
            if(lCostingId != 0){
                   sql += " WHERE "+PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_COSTING_ID]+" = "+lCostingId+
                        " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID];
            }else{
                   sql +=  " GROUP BY "+PstCosting.fieldNames[PstCosting.FLD_CONTACT_ID];
            }
            System.out.println("sql listCostingAdjDetail ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                double dDiscount = 0;
                double dAmount = 0;
                double dNetAmount = 0;
                dDiscount = rs.getDouble("DISCOUNT");
                dAmount = rs.getDouble("AMOUNT");
                dNetAmount = dAmount - dDiscount;
                CostingAdjustment objCostingAdjustment = new CostingAdjustment();
                objCostingAdjustment.setContactId(rs.getLong(PstCostingAdjustment.fieldNames[PstCostingAdjustment.FLD_CONTACT_ID]));
                objCostingAdjustment.setNetAmount(dNetAmount); 
                vResult.add(objCostingAdjustment);
            }
            rs.close();
            return vResult;
        }catch(Exception e){            
            System.out.println("Exception on SessInvoice.listInvoiceDetail() ::: "+e.toString());
        }
        return new Vector(1,1);
    }    
    
    public static void main(String[] arg){
        SessCosting objSessCosting = new SessCosting();
        CostingAdjustment objCostingAdj = new CostingAdjustment();
        try{
            objCostingAdj = PstCostingAdjustment.fetchExc(504404353197374348L);
        }catch(Exception e){}
        
       /* Costing objCosting = new Costing();
        try{
            objCosting = PstCosting.fetchExc(504404353197043161L);
        }catch(Exception e){}*/
        
        try{
            //objSessCosting.postingJournalCosting(504404270678693270L,504404248046790125L,20070801L,504404353118274052L,504404270678693270L,9900L,objCosting); 
            //objSessCosting.postingJurnalCostingAdj(504404270678693270L, 504404248046790125L, 20070801L, objCostingAdj, 504404270678693270L, 9900L);
            //objSessCosting.postingAccPayable(504404353200061177L, objCosting, 504404270678693270L);
            objSessCosting.postingAdjAccPayable(504404353200061177L,objCostingAdj,504404270678693270L);
            //objSessCosting.postingPaymentTerm(504404353197043161L);
            System.out.println("SUKSES ::::: ");
        }catch(Exception e){ System.out.println("GAGAL ::::: ");}
    }
}

