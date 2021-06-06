/*
 * SessInvoice.java
 *
 * Created on November 12, 2007, 2:45 PM
 */

package com.dimata.aiso.session.invoice;

/**
 *
 * @author  dwi
 */
import com.dimata.aiso.entity.invoice.*;
import com.dimata.aiso.entity.costing.*;
import com.dimata.aiso.entity.arap.*;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.PstTicketDeposit;
import com.dimata.aiso.entity.masterdata.PstTicketList;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.ArapCheck;
import com.dimata.aiso.entity.report.GLCheck;
import com.dimata.aiso.entity.report.PaymentCheck;
import com.dimata.aiso.entity.report.ReservationPackageList;
import com.dimata.aiso.entity.report.SalesTicketList;
import com.dimata.aiso.entity.search.SrcInvoice;
import com.dimata.aiso.entity.search.SrcReservationPackageList;
import com.dimata.aiso.entity.search.SrcSalesTicketList;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.aiso.session.costing.SessCosting;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessInvoice {
   public static final String SESS_INVOICE = "SESS_INVOICE";    
  
	   
   /**
    * Created by : DWI
    * This method is used to posted invoice to journal
    * @param long bookType
    * @param long userOID
    * @param long periodeOID
    * @param InvoiceMain objInvoiceMain
    * @param long currencyTypeId
    * @param long departmentId
    * @return int iPosting
    */
    public static synchronized int postingJournalInvoice(long bookType, long userOID, long periodeOID, InvoiceMain objInvoiceMain, long lCurrType, long lDepartmentId){
        try{
            if(objInvoiceMain != null && objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                JurnalUmum objJurnalUmum = new JurnalUmum();
                objJurnalUmum.setBookType(bookType);
                objJurnalUmum.setContactOid(objInvoiceMain.getFirstContactId());
                objJurnalUmum.setCurrType(lCurrType);
                objJurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
                objJurnalUmum.setKeterangan("Invoice Issued on : "+objInvoiceMain.getIssuedDate()+", Invoice no : "+objInvoiceMain.getInvoiceNumber());
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


                Vector vListDetail = listInvoiceDetail(objInvoiceMain.getOID());
                JurnalDetail objJurnalDetail = new JurnalDetail();
                InvoiceDetail objInvoiceDetail = new InvoiceDetail();
                double dTotalAmount = 0;
                int idx = 0;

                //Insert jurnal detail kredit
                if(vListDetail != null && vListDetail.size() > 0){
                    for(int i = 0; i < vListDetail.size(); i++){
                        objInvoiceDetail = (InvoiceDetail)vListDetail.get(i);
                        objJurnalDetail = new JurnalDetail();
                        objJurnalDetail.setIndex(idx);
                        objJurnalDetail.setIdPerkiraan(objInvoiceDetail.getIdPerkiraan());
                        objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                        objJurnalDetail.setCurrType(lCurrType);
                        objJurnalDetail.setCurrAmount(1);
                        objJurnalDetail.setDebet(0);
                        objJurnalDetail.setKredit(objInvoiceDetail.getNetAmount());
                        objJurnalUmum.addDetails(i, objJurnalDetail);
                        idx++;                       
                        dTotalAmount += objInvoiceDetail.getNetAmount();
                    }
                }

                //Insert jurnal detail debet
                objJurnalDetail = new JurnalDetail();
                objJurnalDetail.setIndex(0);
                objJurnalDetail.setIdPerkiraan(objInvoiceMain.getIdPerkiraan());
                objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                objJurnalDetail.setCurrType(lCurrType);
                objJurnalDetail.setCurrAmount(1);
                objJurnalDetail.setDebet(dTotalAmount - objInvoiceMain.getTax());
                objJurnalDetail.setKredit(0);
                objJurnalUmum.addDetails(0, objJurnalDetail);
                
                objJurnalDetail = new JurnalDetail();
                objJurnalDetail.setIndex(1);
                objJurnalDetail.setIdPerkiraan(objInvoiceMain.getIdPerkDeposit());
                objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                objJurnalDetail.setCurrType(lCurrType);
                objJurnalDetail.setCurrAmount(1);
                objJurnalDetail.setDebet(objInvoiceMain.getTax());
                objJurnalDetail.setKredit(0);
                objJurnalUmum.addDetails(0, objJurnalDetail);

                CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
                try{
                    ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, objJurnalUmum);
                    return 1;
                }catch(Exception e){
                    System.out.println("Exception pd saat posting :::: "+e.toString());
                }
            }  
        }catch(Exception e){
            System.out.println("Exception on postingJournalInvoice() :::: "+e.toString());
        }
        
        return 0;
   } 
   
   public static synchronized int postingAccReceivable(InvoiceMain objInvoiceMain, long lCurrencyId){      
        try{
             if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                 double dAmount = 0;
                 Vector vDetail = listInvoiceDetail(objInvoiceMain.getOID());
                 InvoiceDetail objInvoiceDetail = new InvoiceDetail();
                 if(vDetail != null && vDetail.size() > 0){
                    for(int i=0; i < vDetail.size(); i++){
                        objInvoiceDetail = (InvoiceDetail)vDetail.get(i);
                        dAmount += objInvoiceDetail.getNetAmount();
                    }
                 }
                 ArApMain objArapMain = new ArApMain();                   
                    objArapMain.setAmount(dAmount - objInvoiceMain.getTax());
                    objArapMain.setArApDocStatus(PstArApMain.STATUS_CLOSED);
                    objArapMain.setArApType(PstArApMain.TYPE_AR);
                    objArapMain.setContactId(objInvoiceMain.getFirstContactId());
                    objArapMain.setDescription("Account Receivable Invoice No. : "+objInvoiceMain.getInvoiceNumber()+
                    ", Issued Date : "+Formater.formatDate(objInvoiceMain.getIssuedDate(),"dd MMM yyyy"));
                    objArapMain.setIdCurrency(lCurrencyId);
                    objArapMain.setIdPerkiraan(objInvoiceMain.getIdPerkiraan());
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
                                             " AND "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_TYPE]+" = "+PstPaymentTerm.PAY_TERM_AR;
                        Vector vPayTerm = PstPaymentTerm.list(0,0, strWhClause, "");
                        PaymentTerm objPaymentTerm = new PaymentTerm();
                        ArApItem objArApItem = new ArApItem();
                        if(vPayTerm != null && vPayTerm.size() > 0){
                            for(int p = 0; p < vPayTerm.size(); p++){
                                objPaymentTerm = (PaymentTerm)vPayTerm.get(p);
                                objArApItem = new ArApItem();
                                objArApItem.setAngsuran(objPaymentTerm.getAmount());
                                objArApItem.setArApMainId(objArapMain.getOID());
                                objArApItem.setArApItemStatus(0);
                                objArApItem.setCurrencyId(lCurrencyId);
                                objArApItem.setDescription(objPaymentTerm.getNote());
                                objArApItem.setDueDate(objPaymentTerm.getPlanDate());
                                objArApItem.setLeftToPay(objPaymentTerm.getAmount());
                                objArApItem.setRate(1);
                                objArApItem.setReceiveAktivaId(0);
                                objArApItem.setSellingAktivaId(0);
                                
                                    try{
                                        long lInsertItem = PstArApItem.insertExc(objArApItem);
                                        objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                        objArapMain.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                        long lUpdateMain = PstArApMain.updateExc(objArapMain);
                                        objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                        long lUpdateInvoiceMain = PstInvoiceMain.updateExc(objInvoiceMain);
                                        return 1;
                                    }catch(Exception e){
                                        System.out.println("Exception on SessInvoice.insertArApItem :::: "+e.toString());
                                    }     
                                }
                            }
                        }
                    }catch(Exception e){
                            System.out.println("Exception on SessInvoice.insertARMain :::: "+e.toString());
                 }
             }
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingAccReceivable() ::: "+e.toString());
        }
        
        return 0;
   }
   
    public void postingAdjAccReceivable(long lJurnalUmumId, InvoiceAdjMain objInvoiceAdjMain, long lCurrencyId){      
        try{
            System.out.println("lJurnalUmumId : "+lJurnalUmumId+", objInvoiceAdjMain.getStatus() : "+objInvoiceAdjMain.getStatus()+
            ", I_DocStatus.STS_FINAL : "+I_DocStatus.DOCUMENT_STATUS_FINAL);
             if(lJurnalUmumId != 0 && objInvoiceAdjMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                 double dAmount = 0;
                 Vector vDetail = listInvoiceAdjDetail(objInvoiceAdjMain.getOID());
                 InvoiceAdjDetail objInvoiceAdjDetail = new InvoiceAdjDetail();
                 if(vDetail != null && vDetail.size() > 0){
                    for(int i=0; i < vDetail.size(); i++){
                        objInvoiceAdjDetail = (InvoiceAdjDetail)vDetail.get(i);
                        dAmount += objInvoiceAdjDetail.getNetAmount();
                    }
                 }
                    InvoiceMain objInvoiceMain = PstInvoiceMain.fetchExc(objInvoiceAdjMain.getInvoiceId());
                    ArApMain objArapMain = new ArApMain();                   
                    objArapMain.setAmount(dAmount);
                    objArapMain.setArApDocStatus(PstArApMain.STATUS_CLOSED);
                    objArapMain.setArApType(PstArApMain.TYPE_AR);
                    objArapMain.setContactId(objInvoiceMain.getFirstContactId());
                    objArapMain.setDescription("Adjustment for Invoice No. : "+objInvoiceMain.getInvoiceNumber()+
                    ", Issued Date : "+Formater.formatDate(objInvoiceMain.getIssuedDate(),"dd MMM yyyy"));
                    objArapMain.setIdCurrency(lCurrencyId);
                    objArapMain.setIdPerkiraan(objInvoiceMain.getIdPerkiraan());
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
                    }catch(Exception e){
                        System.out.println("Exception on SessInvoice.insertARAdjMain :::: "+e.toString());
                    }
                                        
                    if(objArapMain.getOID() != 0){
                        ArApItem objArApItem = new ArApItem();
                        objArApItem.setAngsuran(dAmount);
                        objArApItem.setArApMainId(objArapMain.getOID());
                        objArApItem.setArApItemStatus(0);
                        objArApItem.setCurrencyId(lCurrencyId);
                        objArApItem.setDescription("Adjustment Invoice No."+objInvoiceMain.getInvoiceNumber());
                        objArApItem.setDueDate(objInvoiceAdjMain.getDate());
                        objArApItem.setLeftToPay(0);
                        objArApItem.setRate(1);
                        objArApItem.setReceiveAktivaId(0);
                        objArApItem.setSellingAktivaId(0);

                        try{
                            long lOid = PstArApItem.insertExc(objArApItem);
                            objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                            objArapMain.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                            long lUpdateMain = PstArApMain.updateExc(objArapMain);
                        }catch(Exception e){
                            System.out.println("Exception on SessInvoice.insertArApItem :::: "+e.toString());
                        }     
                    }                       
             }
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingAccReceivable() ::: "+e.toString());
        }       
   }
    
    public void postingPaymentTerm(long lInvoiceMainId){
        try{
            InvoiceMain objInvoiceMain = new InvoiceMain();
            try{
                objInvoiceMain = PstInvoiceMain.fetchExc(lInvoiceMainId);
            }catch(Exception e){
                System.out.println("Exception on fetch ObjInvoiceMain ::: "+e.toString());
            }
            
            double dAmount = 0;
            Vector vDetail = (Vector)listInvoiceDetail(lInvoiceMainId);
            if(vDetail != null && vDetail.size() > 0){
                for(int i = 0; i < vDetail.size(); i++){
                    InvoiceDetail objInvoiceDetail = (InvoiceDetail)vDetail.get(i);
                    dAmount = objInvoiceDetail.getNetAmount();
                }
            }
            
            Date issuedDate = objInvoiceMain.getIssuedDate();
            int iDate = issuedDate.getDate();
            int iDueDate = iDate + objInvoiceMain.getTermOfPayment();
            
            Date dueDate = (Date)issuedDate.clone();
            dueDate.setDate(iDueDate);
            
            PaymentTerm objPaymentTerm = new PaymentTerm();
            objPaymentTerm.setAmount(dAmount);
            objPaymentTerm.setInvoiceId(objInvoiceMain.getOID());
            objPaymentTerm.setNote("Plan payment for invoice no : "+objInvoiceMain.getInvoiceNumber());
            objPaymentTerm.setPlanDate(dueDate);
            objPaymentTerm.setType(0);
            
            try{
                long lOid = PstPaymentTerm.insertExc(objPaymentTerm);
            }catch(Exception e){
                System.out.println("Exception on SessInvoice.InsertObjPaymentTerm :::: "+e.toString());
            }
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingPaymentTerm() ::: "+e.toString());
        }
    }
    
    public void postingJurnalAdjustment(long bookType, long userOID, long periodeOID, long lAdjustmentId, long lCurrType){
        try{
            InvoiceAdjMain objInvoiceAdjMain = PstInvoiceAdjMain.fetchExc(lAdjustmentId);
            InvoiceMain objInvoiceMain = PstInvoiceMain.fetchExc(objInvoiceAdjMain.getInvoiceId());
            
            JurnalUmum objJurnalUmum = new JurnalUmum();
            objJurnalUmum.setBookType(bookType);
            objJurnalUmum.setContactOid(objInvoiceMain.getFirstContactId());
            objJurnalUmum.setCurrType(lCurrType);
            objJurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            objJurnalUmum.setKeterangan("Adjustment invoice no : "+objInvoiceMain.getInvoiceNumber()+", Date issued : "+objInvoiceMain.getIssuedDate());
            objJurnalUmum.setPeriodeId(periodeOID);
            //String strJournalNumber = SessJurnal.generateVoucherNumber(periodeOID, objInvoiceMain.getIssuedDate());
            //objJurnalUmum.setSJurnalNumber(strJournalNumber);
            objJurnalUmum.setTglEntry(new Date());
            objJurnalUmum.setTglTransaksi(objInvoiceAdjMain.getDate());
            objJurnalUmum.setUserId(userOID);
            //objJurnalUmum.setVoucherCounter(Integer.parseInt(strJournalNumber.substring(5)));
            //objJurnalUmum.setVoucherNo(strJournalNumber.substring(0,4));
            objJurnalUmum.setReferenceDoc(objInvoiceMain.getInvoiceNumber());
            
            double dAmount = 0;
            double dAmountMinus = 0;
            double dAmountPlus = 0;
            int idx = 0;
            Vector vDetail = (Vector)listInvoiceAdjDetail(lAdjustmentId);  
            JurnalDetail objJurnalDetail = new JurnalDetail();
            for(int i = 0; i < vDetail.size(); i++){
                InvoiceAdjDetail objInvoiceAdjDetail = (InvoiceAdjDetail)vDetail.get(i);
                objJurnalDetail = new JurnalDetail();
                dAmount = objInvoiceAdjDetail.getNetAmount();
                if(dAmount < 0){   
                    objJurnalDetail.setIndex(idx);
                    objJurnalDetail.setIdPerkiraan(objInvoiceAdjDetail.getIdPerkiraan());
                    objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    objJurnalDetail.setCurrType(lCurrType);
                    objJurnalDetail.setCurrAmount(1);
                    objJurnalDetail.setDebet(objInvoiceAdjDetail.getNetAmount() * -1);
                    objJurnalDetail.setKredit(0);
                    objJurnalUmum.addDetails(i, objJurnalDetail);
                    dAmountMinus += objInvoiceAdjDetail.getNetAmount();
                    idx++;                    
                }else{
                    objJurnalDetail.setIndex(idx);
                    objJurnalDetail.setIdPerkiraan(objInvoiceAdjDetail.getIdPerkiraan());
                    objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    objJurnalDetail.setCurrType(lCurrType);
                    objJurnalDetail.setCurrAmount(1);
                    objJurnalDetail.setDebet(0);
                    objJurnalDetail.setKredit(objInvoiceAdjDetail.getNetAmount());
                    objJurnalUmum.addDetails(i, objJurnalDetail);
                    dAmountPlus += objInvoiceAdjDetail.getNetAmount();
                    idx++;           
                }
            }
            
            double dTotPiutang = dAmountPlus + dAmountMinus;
            objJurnalDetail = new JurnalDetail();
            objJurnalDetail.setIndex(0);
            objJurnalDetail.setIdPerkiraan(objInvoiceMain.getIdPerkiraan());
            objJurnalDetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            objJurnalDetail.setCurrType(lCurrType);
            objJurnalDetail.setCurrAmount(1);
            if(dTotPiutang < 0){
                objJurnalDetail.setDebet(0);
                objJurnalDetail.setKredit(dTotPiutang * -1);
            }else{
                objJurnalDetail.setDebet(dTotPiutang);
                objJurnalDetail.setKredit(0);
            }
            objJurnalUmum.addDetails(idx, objJurnalDetail);
            
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, objJurnalUmum);
            try{
                objInvoiceAdjMain.setStatus(I_DocStatus.DOCUMENT_STATUS_POSTED); 
                long lOid = PstInvoiceAdjMain.updateExc(objInvoiceAdjMain);
            }catch(Exception e){
                System.out.println("Exception on update InvoiceAdjMain ::::: "+e.toString());
            }
        }catch(Exception e){
            System.out.println("Exception on SessInvoice.postingJurnalAdjustment() ::: "+e.toString());
        }
    }
    
    private static Vector listInvoiceDetail(long lInvoiceId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ID_PERKIRAAN]+
                        ", SUM("+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ITEM_DISCOUNT]+
                        ") AS DISCOUNT "+
                        ", SUM("+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+
                        " * "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_UNIT_PRICE]+
                        ") AS AMOUNT FROM "+PstInvoiceDetail.TBL_INV_DETAIL;
            if(lInvoiceId != 0){
                   sql += " WHERE "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+" = "+lInvoiceId+
                        " GROUP BY "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ID_PERKIRAAN];
            }else{
                   sql += " GROUP BY "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ID_PERKIRAAN];
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                double dDiscount = 0;
                double dAmount = 0;
                double dNetAmount = 0;
                dDiscount = rs.getDouble("DISCOUNT");
                dAmount = rs.getDouble("AMOUNT");
                dNetAmount = dAmount - dDiscount;
                InvoiceDetail objInvoiceDetail = new InvoiceDetail();
                objInvoiceDetail.setIdPerkiraan(rs.getLong(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ID_PERKIRAAN]));
                objInvoiceDetail.setNetAmount(dNetAmount);
                vResult.add(objInvoiceDetail);
            }
            rs.close();
            return vResult;
        }catch(Exception e){            
            System.out.println("Exception on SessInvoice.listInvoiceDetail() ::: "+e.toString());
        }
        return new Vector(1,1);
    }
    
    private Vector listInvoiceAdjDetail(long lAdjustmentId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT "+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ID_PERKIRAAN]+
                        ", SUM("+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ITEM_DISCOUNT]+
                        ") AS DISCOUNT "+
                        ", SUM("+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_NUMBER]+
                        " * "+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_UNIT_PRICE]+
                        ") AS AMOUNT FROM "+PstInvoiceAdjDetail.TBL_INV_ADJ_DETAIL;
            if(lAdjustmentId != 0){
                   sql += " WHERE "+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ADJUSTMENT_ID]+" = "+lAdjustmentId+
                        " GROUP BY "+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ID_PERKIRAAN];
            }else{
                   sql += " GROUP BY "+PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ID_PERKIRAAN];
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                double dDiscount = 0;
                double dAmount = 0;
                double dNetAmount = 0;
                dDiscount = rs.getDouble("DISCOUNT");
                dAmount = rs.getDouble("AMOUNT");
                dNetAmount = dAmount - dDiscount;
                InvoiceAdjDetail objInvoiceAdjDetail = new InvoiceAdjDetail();
                objInvoiceAdjDetail.setIdPerkiraan(rs.getLong(PstInvoiceAdjDetail.fieldNames[PstInvoiceAdjDetail.FLD_ID_PERKIRAAN]));
                objInvoiceAdjDetail.setNetAmount(dNetAmount); 
                vResult.add(objInvoiceAdjDetail);
            }
            rs.close();
            return vResult;
        }catch(Exception e){            
            System.out.println("Exception on SessInvoice.listInvoiceDetail() ::: "+e.toString());
        }
        return new Vector(1,1);
    }    
   
    public static int getLastCounter(String prefikVoucer){
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT MAX("+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_NUMBER_COUNTER]+")"+
                        " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN+
                        " WHERE "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
                        " LIKE '%"+prefikVoucer+"%'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){}
        return 1;
    }
    
    public static int getLastCounter(){
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT MAX("+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_NUMBER_COUNTER]+")"+
                        " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){}
        return 1;
    }
    
    public static String genVoucherNumber(long lPeriodId, Date transDate){
        try{
            if(PstPeriode.getCurrPeriodId() == lPeriodId){ 
                return SessJurnal.generatePrefiksVoucher(transDate)+"-"+SessJurnal.intToStr((getLastCounter() + 1), 4);
            }else{
                return SessJurnal.generatePrefiksVoucher(transDate)+"-0001";
            }
        }catch(Exception e){}
        return "";
    }
   
    public static String genInvoiceNumber(Date transDate){
        long lPeriodeId = 0;
        Periode objPeriode = new Periode();
        Date lastPeriodDate = new Date();
        try{
            lPeriodeId = PstPeriode.getCurrPeriodId();
            if(lPeriodeId != 0){
                objPeriode = PstPeriode.fetchExc(lPeriodeId);
                if(objPeriode != null){
                    lastPeriodDate = (Date)objPeriode.getTglAkhir();
                    String strPrefik = SessJurnal.generatePrefiksVoucher(transDate);
                    if(transDate.after(lastPeriodDate)){                        
                        String strMonth = strPrefik.substring(2,4);
                        int iMonth = Integer.parseInt(strMonth);
                        int iNewMonth = transDate.getMonth() + 1;
                        if(iNewMonth == iMonth){    
                            return SessJurnal.generatePrefiksVoucher(transDate)+"-"+SessJurnal.intToStr((getLastCounter(strPrefik) + 1), 4);                            
                        }else{
                            return SessJurnal.generatePrefiksVoucher(transDate)+"-"+SessJurnal.intToStr(1, 4);
                        }
                    }else{
                         return SessJurnal.generatePrefiksVoucher(transDate)+"-"+SessJurnal.intToStr((getLastCounter(strPrefik) + 1), 4);
                    }
                }
            }
        }catch(Exception e){}
        return "";
    }
   
    public static Vector listInvoice(int start, int recordToGet, SrcInvoice srcInvoice, String orderBy){
        return listInvoice(start, recordToGet, srcInvoice, orderBy, 0);
    }
    
    public static Vector listInvoice(int start, int recordToGet, SrcInvoice srcInvoice, String orderBy, int iStatus){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
                        ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+
                        ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+
                        ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+
                        ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                        ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+
                        ", SUM(ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+
                        " * ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_UNIT_PRICE]+") AS BRUTO "+
                        ", SUM(ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+
                        " * ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ITEM_DISCOUNT]+") AS DISCOUNT "+
                        " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN+" AS IM "+
                        " LEFT JOIN "+PstInvoiceDetail.TBL_INV_DETAIL+" AS ID "+
                        " ON IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                        " = ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID];
            
            String strGroupBy = " GROUP BY IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
                                ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+
                                ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+
                                ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+
                                ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+
                                ", IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID];
            
            String strWhClause = "";
            String strLike = "";
            if(DBHandler.DBSVR_TYPE == DBHandler.DBSVR_POSTGRESQL){
                strLike = " ILIKE ";
            }else{
                strLike = " LIKE ";
            }
            
            if(srcInvoice.getInvoice_number() != null && srcInvoice.getInvoice_number().length() > 0){
                strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+strLike+"'%"+srcInvoice.getInvoice_number()+"%'";
            }
            
            if(srcInvoice.getContactId() != 0){
                if(strWhClause != null && strWhClause.length() > 0){
                    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+" = "+srcInvoice.getContactId();
                }else{
                    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+" = "+srcInvoice.getContactId();
                }
            }
            
           
            if(srcInvoice.getDateType() != 0){
                boolean bDate = false;
                bDate = Formater.formatDate(srcInvoice.getEndDate(),"yyyy-MM-dd").equals(Formater.formatDate(srcInvoice.getStartDate(),"yyyy-MM-dd"));
                
                if(srcInvoice.getStartDate() != null && srcInvoice.getEndDate() != null && (srcInvoice.getEndDate().after(srcInvoice.getStartDate()) || bDate)){
                    if(strWhClause != null && strWhClause.length() > 0){
                        strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+" BETWEEN '"+
                                       Formater.formatDate(srcInvoice.getStartDate(),"yyyy-MM-dd")+"' AND '"+
                                       Formater.formatDate(srcInvoice.getEndDate(),"yyyy-MM-dd")+"'"; 
                    }else{
                        strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+" BETWEEN '"+
                                       Formater.formatDate(srcInvoice.getStartDate(),"yyyy-MM-dd")+"' AND '"+
                                       Formater.formatDate(srcInvoice.getEndDate(),"yyyy-MM-dd")+"'"; 
                    }
                }
            }
            
            int iInvoiceType = 0;
            iInvoiceType = srcInvoice.getInvoiceType() - 1;
            if(iInvoiceType > -1){
                    if(strWhClause != null && strWhClause.length() > 0){
                        strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = "+iInvoiceType;
                    }else{
                        strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = "+iInvoiceType;
                    }
            }
            
           if(iStatus > I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED){
               if(srcInvoice.getInvoiceStatus() > I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED){
                    if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();
                    }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();
                    }
               }else{
                    if(iStatus == I_DocStatus.DOCUMENT_STATUS_FINAL){
                        if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
                                            ", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
                        }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
                                            ", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
                        }
                    }else{
			if(iStatus == I_DocStatus.DOCUMENT_STATUS_REVISED){
			    if(strWhClause != null && strWhClause.length() > 0){
				    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL;
			    }else{
				    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL;						
			    }
			}else{
			    if(strWhClause != null && strWhClause.length() > 0){
				    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_CANCELLED+
						", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
			    }else{
				    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_CANCELLED+
						", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
			    }
			}
                    }
               }
            }else{
                if(iStatus == I_DocStatus.DOCUMENT_STATUS_DRAFT){
                    if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+iStatus;
                    }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+iStatus;
                    }
                }else{
		    if(srcInvoice.getInvoiceStatus() == 0){
		       if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
					    ", "+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
			}else{
			    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
					    ", "+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
			}
		    }else{
			if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();					   
			}else{
			    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();					  
			}
		    }
		}
            }
            
            if(strWhClause != null && strWhClause.length() > 0){
               sql += " WHERE "+strWhClause; 
            }
            
            sql += strGroupBy;
            
             if(orderBy != null && orderBy.length() > 0){
                sql += orderBy;
            }
            
            switch(DBHandler.DBSVR_TYPE){
               case DBHandler.DBSVR_MYSQL:
                if(start == 0 && recordToGet == 0)
                    sql += "";
                else
                    sql += " LIMIT "+start+", "+recordToGet;
               break;
               case DBHandler.DBSVR_POSTGRESQL:
                 if(start == 0 && recordToGet == 0)
                     sql += "";
                 else
                     sql += " LIMIT "+recordToGet+" OFFSET "+start;
               break;
               case DBHandler.DBSVR_SYBASE:
               break;
               case DBHandler.DBSVR_ORACLE:
               break;
               case DBHandler.DBSVR_MSSQL:
               break; 
               default:
               break;
            }
            
            System.out.println("SQL SessInvoice.listInvoice IIII ::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double dNetAmount = 0.0;
            int index = 0;
            while(rs.next()){
                InvoiceMain objInvoiceMain = new InvoiceMain();
                Vector vTemp = new Vector(1,1);
                objInvoiceMain.setOID(rs.getLong(5));
                objInvoiceMain.setInvoiceNumber(rs.getString(1));
                objInvoiceMain.setIssuedDate(rs.getDate(2));
                objInvoiceMain.setFirstContactId(rs.getLong(3));
                objInvoiceMain.setStatus(rs.getInt(4));
                objInvoiceMain.setType(rs.getInt(6));
                vTemp.add(objInvoiceMain);
                dNetAmount = rs.getDouble(7) - rs.getDouble(8);
                vTemp.add(""+dNetAmount);
                vResult.add(vTemp);                
            }
        }catch(Exception e){vResult = new Vector(1,1);}
        return vResult;
    }
    
    public static int totalInvoice(SrcInvoice srcInvoice){
        return totalInvoice(srcInvoice, 0);
    }
    
    public static int totalInvoice(SrcInvoice srcInvoice, int iStatus){
        DBResultSet dbrs = null;
        try{
            String sql = " SELECT COUNT(DISTINCT IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+") "+
                        " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN+" AS IM "+
                        " LEFT JOIN "+PstInvoiceDetail.TBL_INV_DETAIL+" AS ID "+
                        " ON IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
                        " = ID."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID];
            String strWhClause = "";
            String strLike = "";
            if(DBHandler.DBSVR_TYPE == DBHandler.DBSVR_POSTGRESQL){
                strLike = " ILIKE ";
            }else{
                strLike = " LIKE ";
            }
            
            if(srcInvoice.getInvoice_number() != null && srcInvoice.getInvoice_number().length() > 0){
                strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+strLike+"'%"+srcInvoice.getInvoice_number()+"%'";
            }
            
            if(srcInvoice.getContactId() != 0){
                if(strWhClause != null && strWhClause.length() > 0){
                    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+" = "+srcInvoice.getContactId();
                }else{
                    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_FIRST_CONTACT_ID]+" = "+srcInvoice.getContactId();
                }
            }
            
            if(srcInvoice.getDateType() != 0){
                if(srcInvoice.getStartDate() != null && srcInvoice.getEndDate() != null && (srcInvoice.getEndDate().after(srcInvoice.getStartDate()))){
                    if(strWhClause != null && strWhClause.length() > 0){
                        strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+" BETWEEN '"+
                                       Formater.formatDate(srcInvoice.getStartDate(),"yyyy-MM-dd")+"' AND '"+
                                       Formater.formatDate(srcInvoice.getEndDate(),"yyyy-MM-dd")+"'"; 
                    }else{
                        strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_ISSUED_DATE]+" BETWEEN '"+
                                       Formater.formatDate(srcInvoice.getStartDate(),"yyyy-MM-dd")+"' AND '"+
                                       Formater.formatDate(srcInvoice.getEndDate(),"yyyy-MM-dd")+"'"; 
                    }
                }
            }
            
             int iInvoiceType = 0;
            iInvoiceType = srcInvoice.getInvoiceType() - 1;
            if(iInvoiceType > -1){
                    if(strWhClause != null && strWhClause.length() > 0){
                        strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = "+iInvoiceType;
                    }else{
                        strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+" = "+iInvoiceType;
                    }
            }
            
          if(iStatus > I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED){
               if(srcInvoice.getInvoiceStatus() > I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED){
                    if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();
                    }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();
                    }
               }else{
                    if(iStatus == I_DocStatus.DOCUMENT_STATUS_FINAL){
                        if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
                                            ", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
                        }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
                                            ", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
                        }
                    }else{                    
                        if(iStatus == I_DocStatus.DOCUMENT_STATUS_REVISED){
			    if(strWhClause != null && strWhClause.length() > 0){
				    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL;
			    }else{
				    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL;						
			    }
			}else{
			    if(strWhClause != null && strWhClause.length() > 0){
				    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_CANCELLED+
						", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
			    }else{
				    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" NOT IN("+I_DocStatus.DOCUMENT_STATUS_CANCELLED+
						", "+I_DocStatus.DOCUMENT_STATUS_DRAFT+")";
			    }
			}
                    }
               }
            }else{
                if(iStatus == I_DocStatus.DOCUMENT_STATUS_DRAFT){
                    if(strWhClause != null && strWhClause.length() > 0){
                            strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+iStatus;
                    }else{
                            strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+iStatus;
                    }
                }else{
		    if(srcInvoice.getInvoiceStatus() == 0){
		       if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
					    ", "+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
			}else{
			    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" IN("+I_DocStatus.DOCUMENT_STATUS_POSTED+
					    ", "+I_DocStatus.DOCUMENT_STATUS_FINAL+")";
			}
		    }else{
			if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();					   
			}else{
			    strWhClause = " IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+" = "+srcInvoice.getInvoiceStatus();					  
			}
		    }
		}
            }
            
            if(strWhClause != null && strWhClause.length() > 0){
                sql += " WHERE "+strWhClause;
            }
            
            //sql += " GROUP BY IM."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID];
            System.out.println("SQL SessInvoice.totalInvoice() :::::::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){}
        return 0;
    }
    
    public static void deleteAll(long lOidInvoiceMain){
        try{
            String sqlDetail = " DELETE FROM "+PstInvoiceDetail.TBL_INV_DETAIL+" WHERE "+
                        PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+" = "+lOidInvoiceMain;
                        System.out.println("SessInvoice.deleteAll().sqlDetail : "+sqlDetail);
                        DBHandler.execUpdate(sqlDetail);
                   
            String sqlPayTerm = " DELETE FROM "+PstPaymentTerm.TBL_PAY_TERM+" WHERE "+
                        PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_INVOICE_ID]+" = "+lOidInvoiceMain;
                        System.out.println("SessInvoice.deleteAll().sqlPayTerm : "+sqlPayTerm);
                        DBHandler.execUpdate(sqlPayTerm);
            
            String sqlCosting = " DELETE FROM "+PstCosting.TBL_COSTING+" WHERE "+
                        PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+lOidInvoiceMain;
                        System.out.println("SessInvoice.deleteAll().sqlCosting : "+sqlCosting);
                        DBHandler.execUpdate(sqlCosting);            
        }catch(Exception e){}
    }
    
    public static synchronized void postedData(long bookType, long userOID, long periodeOID, InvoiceMain objInvoiceMain, long lCurrType, long lDepartmentId){
        try{
            int iPostJournalInv = 0;
            int iPostAr = 0;
            int iPostJournalCost = 0;
            int iPostAp = 0;
            iPostJournalInv = postingJournalInvoice(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId);
            if(iPostJournalInv == 1 && objInvoiceMain.getTermOfPayment() > 0){
                iPostAr = postingAccReceivable(objInvoiceMain, lCurrType);
            }
            
            if(iPostAr == 1){
                iPostJournalCost = SessCosting.postJournalCosting(bookType, userOID, periodeOID, objInvoiceMain, lCurrType, lDepartmentId);
            }
            
            if(iPostJournalCost == 1){
                iPostAp = SessCosting.postAccPayable(objInvoiceMain, lCurrType);
            }
        }catch(Exception e){}
    }
    
    public static synchronized boolean checkApPaymentTerm(long lInvoiceId){
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_PAY_TERM_ID]+
                         " FROM "+PstPaymentTerm.TBL_PAY_TERM+
                         " WHERE "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_INVOICE_ID]+" = "+lInvoiceId+
                         " AND "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_TYPE]+" = "+PstPaymentTerm.PAY_TERM_AP;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                return true;
            }
        }catch(Exception e){}
        return false;
    }
    
    public static synchronized void voidingInvoice(long lIdInvoice){
        try{
            InvoiceMain objInvoiceMain = new InvoiceMain();
            objInvoiceMain = PstInvoiceMain.fetchExc(lIdInvoice);
            if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_CANCELLED);
                long lUpdate = PstInvoiceMain.updateExc(objInvoiceMain);
            }
            
            Vector vCosting = new Vector(1,1);
            String whClause = PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+lIdInvoice;
            vCosting = PstCosting.list(0,0,whClause,"");
            
            if(vCosting != null && vCosting.size() > 0){
                Costing objCosting = new Costing();
                for(int i = 0; i < vCosting.size(); i++){
                    objCosting = (Costing)vCosting.get(i);
                    if(objCosting.getStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
                        objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_CANCELLED);
                        long lUpdateStsCosting = PstCosting.updateExc(objCosting);
                    }
                }
            }
        }catch(Exception e){}
    }
    
    public static void updateStatusInvoice(long lIdInvoice){
	if(lIdInvoice != 0){
	    InvoiceMain objInvoiceMain = new InvoiceMain();
	    try{
		objInvoiceMain = PstInvoiceMain.fetchExc(lIdInvoice);
		if(objInvoiceMain != null && objInvoiceMain.getStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){ 
		    objInvoiceMain.setStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
		    long lUpdate = PstInvoiceMain.updateExc(objInvoiceMain);
		}
	    }catch(Exception e){}
	    
	    if(objInvoiceMain.getStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){
		Costing objCosting = new Costing();
		Vector vCosting = new Vector(1,1);
		try{
		    vCosting = PstCosting.list(0, 0, ""+lIdInvoice, "");
		}catch(Exception e){}
		if(vCosting != null && vCosting.size() > 0){
		    for(int i = 0; i < vCosting.size(); i++){
			objCosting = (Costing)vCosting.get(i);
			objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
			try{
			    long lUpdateCosting = PstCosting.updateExc(objCosting);
			}catch(Exception e){}
		    }
		}
	    }
	}
    }
    
    public static synchronized Vector getArapPaymentData(int arapType, long periodeId, String whClause){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Date startDate = new Date();
	Date endDate = new Date();
	Periode objPeriode = new Periode();
	String strWhClause = "";
	try{
	    String sql = " SELECT A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]+
			 ", A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]+
			 ", A."+PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]+
			 ", A."+PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN]+
			 ", P."+PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]+
			 ", P."+PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]+
			 ", P."+PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]+
			 " FROM "+PstArApMain.TBL_ARAP_MAIN+" AS A "+
			 " INNER JOIN "+PstArApPayment.TBL_ARAP_PAYMENT+" AS P "+
			 " ON A."+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]+
			 " = P."+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID];
	    
	    if(arapType >= 0){
		    strWhClause = " WHERE A."+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE]+
				  " = "+arapType;
	    }
	    
	    if(periodeId != 0){
		try{
		    objPeriode = PstPeriode.fetchExc(periodeId);
		    if(objPeriode != null){
			startDate = (Date)objPeriode.getTglAwal();
			endDate = (Date)objPeriode.getTglAkhir();
			if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND P."+PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]+
					   " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+
					   Formater.formatDate(endDate,"yyyy-MM-dd")+"' ";
			}else{
			    strWhClause = " WHERE P."+PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]+
					   " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+
					   Formater.formatDate(endDate,"yyyy-MM-dd")+"' ";
			}
		    }
		}catch(Exception e){}
	    }
	    
	    if(strWhClause != null && strWhClause.length() > 0){
		sql += strWhClause;
	    }
	    
	    if(whClause != null && whClause.length() > 0){
		sql += whClause;
	    }
	    
	    //System.out.println("SessInvoice.getArapPaymentData :::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		PaymentCheck objPaymentCheck = new PaymentCheck();
		objPaymentCheck.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
		objPaymentCheck.setPaymentNo(rs.getString(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]));
		objPaymentCheck.setNotaDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
		objPaymentCheck.setPaymentDate(rs.getDate(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]));
		objPaymentCheck.setAmount(rs.getDouble(PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]));
		objPaymentCheck.setContactId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]));
		objPaymentCheck.setAccountId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN]));
		vResult.add(objPaymentCheck);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getArapData(int arapType, long periodeId, String whClause){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Periode objPeriode = new Periode();
	Date startDate = new Date();
	Date endDate = new Date();
	try{
	    String sql = " SELECT A."+PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN_LAWAN]+
			", C."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+
			", A."+PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]+
			" FROM "+PstArApMain.TBL_ARAP_MAIN+" AS A "+
			" LEFT JOIN "+PstContactList.TBL_CONTACT_LIST+" AS C "+
			" ON A."+PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]+
			" = C."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
	    String strWhClause = "";
	    if(arapType >= 0){
			strWhClause = " WHERE A."+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE]+
				" = "+arapType;
	    }
	    
	    if(periodeId != 0){
		try{
		    objPeriode = PstPeriode.fetchExc(periodeId);
		    if(objPeriode != null){
			startDate = objPeriode.getTglAwal();
			endDate = objPeriode.getTglAkhir();
			if(strWhClause != null && strWhClause.length() > 0){
			    strWhClause += " AND A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]+
					" BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' "+
					" AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"' ";
			}else{
			    strWhClause = " WHERE A."+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]+
					" BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' "+
					" AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"' ";
			}
		    }
		}catch(Exception e){}	
	    }
	    
	    if(strWhClause != null && strWhClause.length() > 0){
		    sql += strWhClause;
	    }
	    
	    if(whClause != null && whClause.length() > 0){
		    sql += whClause;
	    }
	    
	    //System.out.println("SessInvoice.getArapData :::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		ArapCheck objArapCheck = new ArapCheck();
		objArapCheck.setVoucherNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]));
		objArapCheck.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
		objArapCheck.setNotaDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
		objArapCheck.setVoucherDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE]));
		objArapCheck.setContactName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
		objArapCheck.setAmount(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]));
		objArapCheck.setAccountId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN]));
		objArapCheck.setContactId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]));
		objArapCheck.setIdOppAccount(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN_LAWAN]));
		vResult.add(objArapCheck);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getGLData(String strWhClause, int arapType, int debetCredit){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	    try{
		String sql = " SELECT DISTINCT M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+
			    ", C."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
		if(arapType == PstArApMain.TYPE_AR){
		    if(debetCredit == PstPerkiraan.ACC_DEBETSIGN){
			sql += ", SUM(D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS AMOUNT ";
		    }else{
			sql += ", SUM(D."+  PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS AMOUNT ";  
		    }
		}else{
		    if(debetCredit == PstPerkiraan.ACC_DEBETSIGN){
			sql += ", SUM(D."+  PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS AMOUNT ";			
		    }else{
			sql += ", SUM(D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS AMOUNT ";  
		    }
		}
			sql += " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS M "+
				" INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS D "+
				" ON M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
				" = D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
				" INNER JOIN "+PstContactList.TBL_CONTACT_LIST+" AS C "+
				" ON M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+
				" = C."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
			
		
		
		if(strWhClause != null && strWhClause.length() > 0){
		    sql += strWhClause;
		    sql += " GROUP BY M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]+
			    ", M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+
			    ", C."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
		}
		
		//System.out.println("SessInvoice.getGLData :::::::::::: "+sql);	
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    GLCheck objGLCheck = new GLCheck();
		    objGLCheck.setTransDate(rs.getDate(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]));
		    objGLCheck.setDokRef(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]));
		    objGLCheck.setJournalNo(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]));
		    objGLCheck.setContactName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
		    objGLCheck.setDescription(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]));
		    objGLCheck.setContactId(rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]));
		    objGLCheck.setAmount(rs.getDouble("AMOUNT"));
		    vResult.add(objGLCheck);
		}
		rs.close();
	    }catch(Exception e){}
	return vResult;
    }
    
    public static synchronized long getIdJournalByDocRef(String docRef, long lAccountId, long lContactId){
	DBResultSet dbrs = null;
	long lResult = 0;
	try{
	    String sql = " SELECT M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
			" FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS M "+
			" INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS D "+
			" ON M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
			" = D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
			" WHERE M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+
			" = '"+docRef+"' AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
			" = "+lAccountId+
			" AND M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+" = "+lContactId; 
	    
	    //System.out.println("SessInvoice.getIdJournalByDocRef :::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();	    
	    while(rs.next()){
		lResult = rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]);
	    }
	    rs.close();
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized Vector getStrJournalId(int arapType, long lPeriode){
	DBResultSet dbrs = null;
	String sResult = "";
	String sReference = "";
	Vector vResult = new Vector();
	Vector vArAp = new Vector(1,1);
	ArapCheck objArapCheck = new ArapCheck();
	    try{
		vArAp = (Vector)getArapData(arapType, lPeriode, "");
		if(vArAp != null && vArAp.size() > 0){
		    for(int i = 0; i < vArAp.size(); i++){
			objArapCheck = (ArapCheck)vArAp.get(i);
			if(objArapCheck != null){
			    long lJournalId = 0;
			    if(objArapCheck.getIdOppAccount() != 0){
				if(objArapCheck.getVoucherNo() != null && objArapCheck.getVoucherNo().length() > 0){
				    sReference = objArapCheck.getVoucherNo();
				}
			    }else{
				if(objArapCheck.getNotaNo() != null && objArapCheck.getNotaNo().length() > 0){
				    sReference = objArapCheck.getNotaNo();
				}
			    }
			    try{
				lJournalId = getIdJournalByDocRef(sReference, objArapCheck.getAccountId(), objArapCheck.getContactId());
				if(lJournalId != 0){
				    if(i == (vArAp.size() - 1)){
					sResult += String.valueOf(lJournalId)+" )"; 
				    }else{
					sResult += String.valueOf(lJournalId)+", ";
				    }
				}
			    }catch(Exception e){}
			}
		    }
		    vResult.add(""+objArapCheck.getAccountId());
		    vResult.add(sResult);
		}
	    }catch(Exception e){}
	return vResult;
    }
    
    public static synchronized String getStrPaymentJournalId(int arapType, long lPeriode){
	String sResult = "";	
	Vector vArapPayment = new Vector(1,1);
	PaymentCheck objPaymentCheck = new PaymentCheck();
	try{
	    vArapPayment = (Vector)getArapPaymentData(arapType, lPeriode, "");
	    if(vArapPayment != null && vArapPayment.size() > 0){
		for(int i = 0; i < vArapPayment.size(); i++){
		    objPaymentCheck = (PaymentCheck)vArapPayment.get(i);
		    if(objPaymentCheck.getPaymentNo() != null && objPaymentCheck.getPaymentNo().length() > 0){
			long lIdJournal = 0;
			try{
			    lIdJournal = getIdJournalByDocRef(objPaymentCheck.getPaymentNo(), objPaymentCheck.getAccountId(), objPaymentCheck.getContactId());
			    if(lIdJournal != 0){
				if(i == (vArapPayment.size() - 1)){
				    sResult += String.valueOf(lIdJournal)+" )";
				}else{
				    sResult += String.valueOf(lIdJournal)+", ";
				}
			    }
			}catch(Exception e){}
		    }
		}
	    }
	}catch(Exception e){}	
	return sResult;
    }
    
    public static synchronized Vector vListCrossCheckResult(int arapType,int debetCredit){
	Vector vResult = new Vector(1,1);
	Vector vArap = new Vector(1,1);
	Periode objPeriode = new Periode();
	Date startDate = new Date();
	Date endDate = new Date();
	String whClause = "";
	String strJournalId = "";
	GLCheck objGLCheck = new GLCheck();
	long lPeriode = 0;
	long lAccountId = 0;
	try{
	    lPeriode = PstPeriode.getCurrPeriodId();
	    if(lPeriode != 0){
		try{
		    objPeriode = PstPeriode.fetchExc(lPeriode);
		    if(objPeriode != null){
			startDate = (Date)objPeriode.getTglAwal();
			endDate = (Date)objPeriode.getTglAkhir();
		    }
		}catch(Exception e){}
	    }
	}catch(Exception e){}
	try{
	    vArap = (Vector)getStrJournalId(arapType, lPeriode);
	    if(vArap != null && vArap.size() > 0){
		lAccountId = Long.parseLong(vArap.get(0).toString());
		whClause = " WHERE D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" = "+lAccountId;
		      
	    }
	    if(debetCredit == PstPerkiraan.ACC_DEBETSIGN){
		if(vArap != null && vArap.size() > 0){		
		    strJournalId = vArap.get(1).toString().toString();
		    if(strJournalId != null && strJournalId.length() > 0){
			if(arapType == PstArApMain.TYPE_AR){
			    whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" > 0";			   
			}else{	
			     whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" > 0";
			}
			whClause += " AND M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
				    " BETWEEN '"+Formater.formatDate(startDate,"yyyy-MM-dd")+
				    "' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+
				    "' AND M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+" NOT IN( "+strJournalId;
				    
		    }
		}
	    }else{
		strJournalId = (String)getStrPaymentJournalId(arapType, lPeriode);
		if(strJournalId != null && strJournalId.length() > 0){
			if(arapType == PstArApMain.TYPE_AR){
			    whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" > 0";
			}else{	
			    whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" > 0";
			}
			    whClause += " AND M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
					" BETWEEN '"+Formater.formatDate(startDate,"yyyy-MM-dd")+
					"' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+
					"' AND M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+" NOT IN( "+strJournalId;
				    
		}
	    }
	    if(strJournalId != null && strJournalId.length() > 0){		
		vResult = (Vector)getGLData(whClause, arapType, debetCredit);
	    }	    
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getListRefDocJournal(int arapType, int debetCredit, long lAccountId, long lPeriodId){
	Vector vResult = new Vector(1,1);
	String whClause = "";
	Periode objPeriode = new Periode();
	Date startDate = new Date();
	Date endDate = new Date();
	if(lPeriodId != 0){
	    try{
		objPeriode = PstPeriode.fetchExc(lPeriodId);
		if(objPeriode != null){
		    startDate = (Date)objPeriode.getTglAwal();
		    endDate = (Date)objPeriode.getTglAkhir();
		}
	    }catch(Exception e){}
	}
	whClause = " WHERE M."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
		    " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+
		    Formater.formatDate(endDate, "yyyy-MM-dd")+"' "+
		    " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" = "+lAccountId;
	if(arapType == PstArApMain.TYPE_AR){
		whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" > 0";
	}else{
		whClause += " AND D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" > 0";
	}
	
	try{
	    vResult = getGLData(whClause, arapType, debetCredit);
	}catch(Exception e){}	
	return vResult;
    }
    
    public static synchronized long getIdArap(int arapType, int refLength, String strRef, long lContactId){
	DBResultSet dbrs = null;	
	long lResult = 0;
	try{
	    String sql = " SELECT "+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]+
			 " FROM "+PstArApMain.TBL_ARAP_MAIN+
			 " WHERE "+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE]+
			 " = "+arapType;
	    if(lContactId != 0){
		 sql += " AND "+PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]+" = "+lContactId;
	    }
	    
	    if(refLength > 8){
		  sql += " AND "+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]+" = '"+strRef+"' ";  
	    }else{
		  sql += " AND "+PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]+" = '"+strRef+"' "; 
	    }
	    
	    //System.out.println("SQL SessInvoice.getIdArap() :::::::::::::::::::::::::: "+sql);
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		lResult = rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]);
	    }
	    rs.close();
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized String getStringIdArap(int arapType, int debetCredit){
	String sResult = "";
	Vector vArap = new Vector(1,1);
	Vector vGL = new Vector(1,1);
	GLCheck objGLCheck = new GLCheck();
	ArapCheck objArapCheck = new ArapCheck();
	long lPeriodId = 0;
	try{
	    lPeriodId = PstPeriode.getCurrPeriodId();	    
	}catch(Exception e){}
	
	if(lPeriodId != 0){
	    try{
		vArap = (Vector)getArapData(arapType, lPeriodId,"");
	    }catch(Exception e){}
	}
	
	if(vArap != null && vArap.size() > 0){
	    for(int i = 0; i < vArap.size(); i++){
		objArapCheck = (ArapCheck)vArap.get(i);	
	    }
	}
	
	if(objArapCheck.getAccountId() != 0){
	    vGL = (Vector)getListRefDocJournal(arapType, debetCredit, objArapCheck.getAccountId(), lPeriodId);
	    if(vGL != null && vGL.size() > 0){
		for(int g = 0; g < vGL.size(); g++){
		    objGLCheck = (GLCheck)vGL.get(g);
		    if(objGLCheck.getDokRef() != null && objGLCheck.getDokRef().length() > 0){
			long lArapId = 0;
			lArapId = getIdArap(arapType, objGLCheck.getDokRef().length(), objGLCheck.getDokRef(), objGLCheck.getContactId());
			if(lArapId != 0){    
			    if(g == (vGL.size() - 1)){
				sResult += String.valueOf(lArapId)+" )";
			    }else{
				sResult += String.valueOf(lArapId)+", ";
			    }
			} 
		    }
		}
	    }
	}
	return sResult;
    }
    
    public static synchronized String getIdPaymentByIdArap(int arapType, int debetCredit){
	DBResultSet dbrs = null;
	String sResult = "";
	String arapMainId = "";
	long lPeriodeId = 0;
	Periode objPeriode = new Periode();	
	Date startDate = new Date();
	Date endDate = new Date();
	try{
	    lPeriodeId = PstPeriode.getCurrPeriodId();	    
	}catch(Exception e){}
	
	if(lPeriodeId != 0){
	    try{
		objPeriode = PstPeriode.fetchExc(lPeriodeId);
		if(objPeriode != null){
		    startDate = (Date)objPeriode.getTglAwal();
		    endDate = (Date)objPeriode.getTglAkhir();
		}
	    }catch(Exception e){}
	}
	
	try{
	    arapMainId = (String)getStringIdArap(arapType, debetCredit);
	}catch(Exception e){}
	
	try{
	    String sql = " SELECT "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]+
			 " FROM "+PstArApPayment.TBL_ARAP_PAYMENT+
			 " WHERE "+PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]+
			 " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+"' AND '"+
			 Formater.formatDate(endDate, "yyyy-MM-dd")+"' ";
	    if(arapMainId != null && arapMainId.length() > 0){
		    sql += " AND "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID]+
			   " IN( "+arapMainId;
	    }
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		long paymentId = 0;
		paymentId = rs.getLong(PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]);
		if(paymentId != 0){		    
		    if(rs.isLast()){
			sResult += String.valueOf(paymentId)+" )";
		    }else{
			sResult += String.valueOf(paymentId)+", ";
		    }		    
		}
	    }
	    rs.close();
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized Vector listCrossCheckToArap(int arapType, int debetCredit){
	Vector vResult = new Vector(1,1);
	String whClause = "";
	String listIdArap = "";
	long lPeriodeId = 0;
	try{
	    lPeriodeId = PstPeriode.getCurrPeriodId();
	}catch(Exception e){}
	try{
	    listIdArap = (String)getStringIdArap(arapType, debetCredit);
	}catch(Exception e){}
	
	if(listIdArap != null && listIdArap.length() > 0){
	    whClause = " AND A."+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]+
			" NOT IN( "+listIdArap;
	}
	
	if(lPeriodeId != 0){
	    try{
		vResult = (Vector)getArapData(arapType, lPeriodeId, whClause);
	    }catch(Exception e){}
	}
	return vResult;
    }
    
    public static synchronized Vector listCrossCheckToArapPayment(int arapType, int debetCredit){
	Vector vResult = new Vector(1,1);
	String sPaymentId = "";
	String whClause = "";
	long lPeriodeId = 0;
	try{
	    lPeriodeId = PstPeriode.getCurrPeriodId();
	}catch(Exception e){}
	
	try{
	    sPaymentId = (String)getIdPaymentByIdArap(arapType, debetCredit);
	}catch(Exception e){}
	
	if(sPaymentId != null && sPaymentId.length() > 0){
	    whClause = " AND P."+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]+
		       " NOT IN( "+sPaymentId;
	}
	
	if(whClause != null && whClause.length() > 0){
	    vResult = (Vector)getArapPaymentData(arapType, lPeriodeId, whClause);
	}	
	return vResult;
    }
    
    public static synchronized String listReservationPackage(int start, int recordToGet, SrcReservationPackageList srcResPack, String cellStyle, String cellStyleOdd){
	DBResultSet dbrs = null;
	String strList = "<tr>";
	try{
	    String sql = " SELECT "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKIN_DATE]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKOUT_DATE]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_GUEST_NAME]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_DESCRIPTION]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_DEPOSIT_TIME_LIMIT]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TAX]+
			 ", "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+
			 " FROM "+PstInvoiceMain.TBL_AISO_INV_MAIN+
			 " WHERE "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TYPE]+
			 " = 1 AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]+
			 " = "+I_DocStatus.DOCUMENT_STATUS_DRAFT;
	    
		  sql += strWhClause(srcResPack);
		  if(recordToGet > 0){
		    sql += stringLimit(start, recordToGet);
		  }
		  
		  sql += stringOrderBy(srcResPack);
		  
		  //System.out.println("SQL SessInvoice.listReservationPackage :::::::::: "+sql);
		  dbrs = DBHandler.execQueryResult(sql);
		  ResultSet rs = dbrs.getResultSet();
		  int index = 0;
		  String style = "";
		  while(rs.next()){
			ReservationPackageList objResPackList = new ReservationPackageList();
			resultToObject(rs, objResPackList);
			double dp = objResPackList.getDownPayment();
			String sDp = "-";
			if(dp > 0){
			    sDp = "Ok";
			}
			
			if((index % 2) == 0){
			    style = cellStyleOdd;
			}else{
			    style = cellStyle;
			}
			
			if(cellStyleOdd.length() > 0 && cellStyle.length() > 0){
			    strList += "<td width=\"30%\" class=\""+style+"\" nowrap>"+Formater.formatDate(objResPackList.getCheckInDate(),"dd MMM yyyy")+" - "+Formater.formatDate(objResPackList.getCheckOutDate(),"dd MMM yyyy")+"</td>";
			    strList += "<td width=\"15%\" class=\""+style+"\" nowrap><a href=\"javascript:cmdEdit('"+objResPackList.getInvoiceId()+"')\">"+(objResPackList.getGuestName() == null? "-" : objResPackList.getGuestName())+"</a></td>";
			    strList += "<td width=\"30%\" class=\""+style+"\">"+(objResPackList.getDescription() == null? "-" : objResPackList.getDescription())+"</td>";
			    strList += "<td width=\"15%\" align=\"center\" class=\""+style+"\">"+(objResPackList.getDepositTimeLimit() == null? Formater.formatDate(new Date(),"dd MMM yyyy") : Formater.formatDate(objResPackList.getDepositTimeLimit(),"dd MMM yyyy"))+"</td>";
			    strList += "<td width=\"5%\" align=\"center\" class=\""+style+"\">"+sDp+"</td>";
			    strList += "<td width=\"5%\" class=\""+style+"\">"+I_DocStatus.fieldDocumentStatus[objResPackList.getDocStatus()]+"</td>";
			}else{
			    strList += "<td width=\"15%\" nowrap>"+Formater.formatDate(objResPackList.getCheckInDate(),"dd MMM yyyy")+" - "+Formater.formatDate(objResPackList.getCheckOutDate(),"dd MMM yyyy")+"</td>";
			    strList += "<td width=\"25%\" nowrap>"+(objResPackList.getGuestName() == null? "-" : objResPackList.getGuestName())+"</td>";
			    strList += "<td width=\"25%\" >"+(objResPackList.getDescription() == null? "-" : objResPackList.getDescription())+"</td>";
			    strList += "<td width=\"15%\" align=\"center\" >"+(objResPackList.getDepositTimeLimit() == null? Formater.formatDate(new Date(),"dd MMM yyyy") : Formater.formatDate(objResPackList.getDepositTimeLimit(),"dd MMM yyyy"))+"</td>";
			    strList += "<td width=\"10%\" align=\"center\" >"+sDp+"</td>";
			    strList += "<td width=\"10%\" >"+I_DocStatus.fieldDocumentStatus[objResPackList.getDocStatus()]+"</td>";
			}
			
			if(rs.isFirst() || rs.isLast()){
			    strList += "</tr>";
			}else{
			    strList += "</tr><tr>";
			}
			
			index++;
		  }
		  rs.close();
	}catch(Exception e){}
	return strList;
    }
    
    public static synchronized void resultToObject(ResultSet rs, ReservationPackageList objResPackList){
	try{
	    objResPackList.setCheckInDate(rs.getDate(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKIN_DATE]));
	    objResPackList.setCheckOutDate(rs.getDate(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKOUT_DATE]));
	    objResPackList.setDepositTimeLimit(rs.getDate(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_DEPOSIT_TIME_LIMIT]));
	    objResPackList.setDescription(rs.getString(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_DESCRIPTION]));
	    objResPackList.setDocStatus(rs.getInt(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_STATUS]));
	    objResPackList.setDownPayment(rs.getDouble(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_TAX]));
	    objResPackList.setGuestName(rs.getString(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_GUEST_NAME]));
	    objResPackList.setInvoiceId(rs.getLong(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]));
	    objResPackList.setInvoiceNumber(rs.getString(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]));
	}catch(Exception e){}
    }
    
    public static synchronized String strWhClause(SrcReservationPackageList srcResPack){
	String sResult = "";
	try{
	    if(srcResPack.getCInDateFr() != null && srcResPack.getCInDateTo() != null){
		if(srcResPack.getCInDateFr().before(srcResPack.getCInDateTo()) || srcResPack.getCInDateFr().equals(srcResPack.getCInDateTo())){
		    sResult += " AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKIN_DATE]+
			       " BETWEEN '"+Formater.formatDate(srcResPack.getCInDateFr(),"yyyy-MM-dd")+
			       "' AND '"+Formater.formatDate(srcResPack.getCInDateTo(),"yyyy-MM-dd")+"' ";
		}
	    }
	    
	    if(srcResPack.getCOutDateFr() != null && srcResPack.getCOutDateTo() != null){
		if(srcResPack.getCOutDateFr().before(srcResPack.getCOutDateTo()) || srcResPack.getCOutDateFr().equals(srcResPack.getCOutDateTo())){
			sResult += " AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKOUT_DATE]+
			       " BETWEEN '"+Formater.formatDate(srcResPack.getCOutDateFr(),"yyyy-MM-dd")+
			       "' AND '"+Formater.formatDate(srcResPack.getCOutDateTo(),"yyyy-MM-dd")+"' ";
		}
	    }
	    
	    String like = "";
	    like = strLike();
	    if(srcResPack.getGuestName() != null && srcResPack.getGuestName().length() > 0){
		    sResult += " AND "+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_GUEST_NAME]+
			       like +" '%"+srcResPack.getGuestName()+"%' ";
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String stringOrderBy(SrcReservationPackageList srcResPack){
	String sResult = " ORDER BY ";
	try{
	    switch(srcResPack.getOrderBy()){
		case 0:
		    sResult += PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKIN_DATE];
		    break;
		case 1:
		    sResult += PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_CHECKOUT_DATE];
		    break;
		case 2:
		    sResult += PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_GUEST_NAME];
		    break;
		case 3:
		    sResult += PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_DEPOSIT_TIME_LIMIT];
		    break;
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized Vector listTicketSales(int start, int recordToGet, SrcSalesTicketList srcSalesTicketList, String cellStyle, String cellStyleOdd){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	String sResult = "<tr>";
	try{
	    String sql = " SELECT * FROM( SELECT DT."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_DATE]+
			 ", DT."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_DESCRIPTION]+
			 ", DT."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NAME_OF_PAX]+
			 ", MN."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]+
			 ", TL."+PstTicketList.fieldNames[PstTicketList.FLD_TICKET_PRICE]+
			 ", TL."+PstTicketList.fieldNames[PstTicketList.FLD_CARRIER_ID]+
			 ", 0 as deposit "+
			 ", DP."+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]+
			 " FROM "+PstInvoiceDetail.TBL_INV_DETAIL+" AS DT "+
			 " INNER JOIN "+PstInvoiceMain.TBL_AISO_INV_MAIN+" AS MN "+
			 " ON DT."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+
			 " = MN."+PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INV_MAIN_ID]+
			 " INNER JOIN "+PstTicketList.TBL_AISO_TICKET_LIST+" AS TL "+
			 " ON DT."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INV_DETAIL_ID]+
			 " = TL."+PstTicketList.fieldNames[PstTicketList.FLD_INVOICE_DETAIL_ID]+
			 " INNER JOIN "+PstTicketDeposit.TBL_AISO_TICKET_DEPOSIT+" AS DP "+
			 " ON TL."+PstTicketList.fieldNames[PstTicketList.FLD_TICKET_DEPOSIT_ID]+
			 " = DP."+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_TICKET_DEPOSIT_ID]+
			 " UNION "+
			 " SELECT null,'','',''"+			 
			 ",0"+
			 ", "+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_CARRIER_ID]+
			 ", "+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_AMOUNT]+
			 ", "+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]+
			 " FROM "+PstTicketDeposit.TBL_AISO_TICKET_DEPOSIT+
			 ") AS UN";
	    
	    String whClause = "";
	    if(srcSalesTicketList != null){
		whClause = strWhClause(srcSalesTicketList);
	    }
	    if(whClause.length() > 0){
		sql += whClause;
	    }
		sql += " ORDER BY UN."+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]+
			", UN."+PstTicketList.fieldNames[PstTicketList.FLD_TICKET_PRICE]+
			", UN."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_DATE];
			
	    if(recordToGet > 0){	
		sql += stringLimit(start, recordToGet);
	    }
		
		System.out.println("SQL listTicketSales ::::::::::::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		int index = 0;
		double dBalance = 0.0;
		double dTotDebet = 0.0;
		double dTotCredit = 0.0;
		String style = "";
		while(rs.next()){
		    SalesTicketList objSalesTicketList = new SalesTicketList();
		    Date transDate = null;
		    String description = rs.getString(PstInvoiceMain.fieldNames[PstInvoiceMain.FLD_INVOICE_NUMBER]);
		    double dDeposit = rs.getDouble("deposit");
		    double dTicketRate = rs.getDouble(PstTicketList.fieldNames[PstTicketList.FLD_TICKET_PRICE]);
		    Date ticketTransDate = rs.getDate(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_DATE]);
		    Date depositDate = rs.getDate(PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]);
		    if((description == null || description.length() == 0) && dDeposit > 0){
			description = "Deposit";
		    }
		    
		    if(dTicketRate > 0){
			transDate = ticketTransDate;
		    }else{
			transDate = depositDate;
		    }
		    
		    if((index % 2) == 0 ){
			style = cellStyleOdd;
		    }else{
			style = cellStyle;
		    }
		    
		    resultToObject(rs, objSalesTicketList, transDate, dDeposit, description);
		  
		    dBalance += objSalesTicketList.getCredit() - objSalesTicketList.getDebet();
		    dTotDebet += objSalesTicketList.getDebet();
		    dTotCredit += objSalesTicketList.getCredit();
		    
		    if((dDeposit + dTicketRate) > 0){
			if(cellStyle.length() > 0 && cellStyleOdd.length() > 0){
			    sResult += "<td width=\"10%\" class=\""+style+"\">"+Formater.formatDate(objSalesTicketList.getTransDate(),"dd-MM-yyyy")+"</td>";
			    sResult += "<td width=\"20%\" class=\""+style+"\">"+(objSalesTicketList.getDescription() == null? "" : objSalesTicketList.getDescription())+"</td>";
			    sResult += "<td width=\"15%\" class=\""+style+"\">"+(objSalesTicketList.getTicketInfo() == null? "" : objSalesTicketList.getTicketInfo())+"</td>";
			    sResult += "<td width=\"10%\" class=\""+style+"\">"+(objSalesTicketList.getPassName() == null? "" : objSalesTicketList.getPassName())+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\">"+Formater.formatNumber(objSalesTicketList.getDebet(),"##,###")+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\">"+Formater.formatNumber(objSalesTicketList.getCredit(),"##,###")+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\">"+Formater.formatNumber(dBalance,"##,###")+"</td>";
			}else{
			    sResult += "<td width=\"10%\">"+Formater.formatDate(objSalesTicketList.getTransDate(),"dd-MM-yyyy")+"</td>";
			    sResult += "<td width=\"20%\">"+(objSalesTicketList.getDescription().length() == 0? "-" : objSalesTicketList.getDescription())+"</td>";
			    sResult += "<td width=\"15%\">"+(objSalesTicketList.getTicketInfo().length() == 0? "-" : objSalesTicketList.getTicketInfo())+"</td>";
			    sResult += "<td width=\"10%\">"+(objSalesTicketList.getPassName().length() == 0? "-" : objSalesTicketList.getPassName())+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\">"+Formater.formatNumber(objSalesTicketList.getDebet(),"##,###")+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\">"+Formater.formatNumber(objSalesTicketList.getCredit(),"##,###")+"</td>";
			    sResult += "<td width=\"15%\" align=\"right\">"+Formater.formatNumber(dBalance,"##,###")+"</td>";
			}
			if(rs.isLast() || rs.isFirst()){
			    sResult += "</tr>";
			}else{
			    sResult += "</tr><tr>";
			}
			 index++;
		    }
		   
		}
		rs.close();
		if(sResult.length() > 4){
		    if(style.equalsIgnoreCase(cellStyleOdd)){
			style = cellStyle;
		    }else{
			style = cellStyleOdd;
		    }
		    if(cellStyle.length() > 0 && cellStyleOdd.length() > 0){
			sResult += "<td width=\"15%\" align=\"center\" colspan=\"4\" class=\""+style+"\"><b>TOTAL</b></td>";
			sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\"><b>"+Formater.formatNumber(dTotDebet,"##,###")+"</b></td>";
			sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\"><b>"+Formater.formatNumber(dTotCredit,"##,###")+"</b></td>";
			sResult += "<td width=\"15%\" align=\"right\" class=\""+style+"\"></td>";
		    }else{
			sResult += "<td width=\"15%\" align=\"center\" colspan=\"4\"><b>TOTAL</b></td>";
			sResult += "<td width=\"15%\" align=\"right\"><b>"+Formater.formatNumber(dTotDebet,"##,###")+"</b></td>";
			sResult += "<td width=\"15%\" align=\"right\"><b>"+Formater.formatNumber(dTotCredit,"##,###")+"</b></td>";
			sResult += "<td width=\"15%\" align=\"right\">-</td>";
		    }
		}
		vResult.add(sResult);
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized void resultToObject(ResultSet rs, SalesTicketList objSalesTicketList, Date transDate, double dDeposit, String strDescription){
	try{
		double dTicketPrice = rs.getDouble(PstTicketList.fieldNames[PstTicketList.FLD_TICKET_PRICE]);
	   
		objSalesTicketList.setTransDate(transDate);
		objSalesTicketList.setTicketInfo(rs.getString(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_DESCRIPTION]));
		objSalesTicketList.setPassName(rs.getString(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NAME_OF_PAX]));
		objSalesTicketList.setDescription(strDescription);
		objSalesTicketList.setDebet(dTicketPrice);
		objSalesTicketList.setCredit(dDeposit);
	  
	}catch(Exception e){}
    }
    
    public static synchronized String strWhClause(SrcSalesTicketList srcSalesTicketList){
	String sResult = "";
	try{
	    if(srcSalesTicketList.getTransDateFr() != null && srcSalesTicketList.getTransDateTo() != null){
		if(srcSalesTicketList.getTransDateFr().before(srcSalesTicketList.getTransDateTo()) 
		  || srcSalesTicketList.getTransDateFr().equals(srcSalesTicketList.getTransDateTo())){
		  sResult = " WHERE UN."+PstTicketDeposit.fieldNames[PstTicketDeposit.FLD_DEPOSIT_DATE]+
			    " BETWEEN '"+Formater.formatDate(srcSalesTicketList.getTransDateFr(),"yyyy-MM-dd")+
			    "' AND '"+Formater.formatDate(srcSalesTicketList.getTransDateTo(),"yyyy-MM-dd")+"' ";
		}
	    }
	    
	    if(srcSalesTicketList.getPassName() != null && srcSalesTicketList.getPassName().length() > 0){
		String like = "";
		like = strLike();
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND UN."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NAME_OF_PAX]+
			        like +" '%"+srcSalesTicketList.getPassName()+"%' ";    
		}else{
		    sResult = " WHERE UN."+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NAME_OF_PAX]+
			        like +" '%"+srcSalesTicketList.getPassName()+"%' "; 
		}
	    }
	    
	    if(srcSalesTicketList.getCarrierId() != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND UN."+PstTicketList.fieldNames[PstTicketList.FLD_CARRIER_ID]+
			        " = "+srcSalesTicketList.getCarrierId();    
		}else{
		    sResult = " WHERE UN."+PstTicketList.fieldNames[PstTicketList.FLD_CARRIER_ID]+
			        " = "+srcSalesTicketList.getCarrierId();
		}
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String stringLimit(int start, int recordToGet){
	String sResult = "";
	try{
	    switch(PstInvoiceMain.DBSVR_TYPE){
		case PstInvoiceMain.DBSVR_MYSQL:
		    sResult += " LIMIT "+start+", "+recordToGet;
		    break;
		case 1:
		    sResult += " LIMIT "+recordToGet+" OFFSET "+start;
		    break;
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String strLike(){
	String sResult = "";
	try{
	    if(DBHandler.DBSVR_TYPE == DBHandler.DBSVR_MYSQL)
		sResult = " LIKE ";
	    else
		sResult = " ILIKE ";
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized double getTotalInvoice(long invoiceId){
	DBResultSet dbrs = null;
	double dResult = 0.0;
	try{
	    String sql = " SELECT SUM("+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+
			 " * "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_UNIT_PRICE]+") AS BRUTO "+
			 ", SUM("+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_NUMBER]+
			 " * "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_ITEM_DISCOUNT]+") AS DISCOUNT "+
			 " FROM "+PstInvoiceDetail.TBL_INV_DETAIL+
			 " WHERE "+PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+" = "+invoiceId;
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		double bruto = rs.getDouble("BRUTO");
		double discount = rs.getDouble("DISCOUNT");
		dResult = bruto - discount;
	    }
	    rs.close();
	}catch(Exception e){}
	return dResult;
    }
        
    
    public static double getTotalHpp(long invoiceId){
	DBResultSet dbrs = null;
	double dHpp = 0.0;
	try{
	    String sql = " SELECT SUM("+PstCosting.fieldNames[PstCosting.FLD_NUMBER]+
			 " * "+PstCosting.fieldNames[PstCosting.FLD_UNIT_PRICE]+") AS COSTING "+
			 ", SUM("+PstCosting.fieldNames[PstCosting.FLD_NUMBER]+
			 " * "+PstCosting.fieldNames[PstCosting.FLD_DISCOUNT]+") AS POTONGAN "+
			 " FROM "+PstCosting.TBL_COSTING+
			 " WHERE "+PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+invoiceId;
	    
	    //System.out.println("SQL SessInvoice.getTotalHpp ::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		double costing = rs.getDouble("costing");
		double potongan = rs.getDouble("potongan");
		dHpp = costing - potongan;
	    }
	    rs.close();
	}catch(Exception e){}
	return dHpp;
    }
    
    public static synchronized long getIdPaymentTerm(long invoiceId, int ArapType){
	DBResultSet dbrs = null;
	long lResult = 0;
	try{
	    String sql = " SELECT "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_PAY_TERM_ID]+
			 " FROM "+PstPaymentTerm.TBL_PAY_TERM+
			 " WHERE "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_INVOICE_ID]+" = "+invoiceId+
			 " AND "+PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_TYPE]+" = "+ArapType;
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		lResult = rs.getLong(PstPaymentTerm.fieldNames[PstPaymentTerm.FLD_PAY_TERM_ID]);
	    }
	    rs.close();
	}catch(Exception e){}
	return lResult;
    }
    
    public static void main(String[] arg){
        PstArApItem pstArApItem = new PstArApItem();
        ArApItem arApItem = new ArApItem();
        Vector vArApItem = new Vector(1,1);
        double dAngsuran = 0.0;
        try{
            vArApItem = pstArApItem.list(0,0, "", "");
        }catch(Exception e){}
        if(vArApItem != null && vArApItem.size() > 0){
            for(int i = 0; i < vArApItem.size(); i++){
                arApItem = (ArApItem)vArApItem.get(i);
                if(arApItem != null){
                    dAngsuran = arApItem.getAngsuran();
                }
                if(dAngsuran > 0){
                    try{
                        arApItem.setLeftToPay(dAngsuran);
                        pstArApItem.updateExc(arApItem);
                        System.out.println("UPDATE SUCCESS :::::::::::::::: ");
                    }catch(Exception e){}
                }
            }
        }
    }
}

