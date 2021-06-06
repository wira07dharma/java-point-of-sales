/*
 * CtrlInvoiceDetail.java
 *
 * Created on November 13, 2007, 4:40 PM
 */

package com.dimata.aiso.form.invoice;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// import dimata
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

// import qdep
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.Control;

// import aiso
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.costing.Costing;
import com.dimata.aiso.entity.costing.PstCosting;
import com.dimata.aiso.entity.invoice.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.entity.masterdata.PstTicketList;
import com.dimata.aiso.entity.masterdata.TicketList;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.session.invoice.SessInvoice;
import com.dimata.aiso.session.jurnal.*;
import com.dimata.qdep.entity.I_DocStatus;

/**
 *
 * @author  dwi
 */
public class CtrlInvoiceDetail extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    public static int RSLT_UPDATE = 1;
    public static int RSLT_UNKNOWN_ERROR = 2;
    public static int RSLT_INVALID_TRANS_DATE = 3;
    public static int RSLT_VOUCHER_NUMBER_EXIST = 4;
    public static int RSLT_FORM_INCOMPLETE = 5;
    
    public static String[][] resultText = {
        {"Data Tersimpan","Data Terupdate","Proses Gagal","Tanggal transaksi tidak sesuai","Nomor sudah ada","Form tidak lengkap"},
        {"Data is saved","Data is updated","Process Failed","Transaction Data Invalid","Invoice Number is Exist","Form Incomplete"}
    };
    
    private int iErrorCode = 0;
    private String msgString;
    private InvoiceDetail objInvoiceDetail;
    private PstInvoiceDetail pstInvoiceDetail;
    private FrmInvoiceDetail frmInvoiceDetail;
    private int iLanguage = LANGUAGE_DEFAULT;
    
    /** Creates a new instance of CtrlInvoiceDetail */
    public CtrlInvoiceDetail() {
    }
    
     public CtrlInvoiceDetail(HttpServletRequest request) {
        msgString = "";
        objInvoiceDetail = new InvoiceDetail();
        try{
            pstInvoiceDetail = new PstInvoiceDetail(0);
        }catch(Exception e){
            System.out.println("Exception on CtrlInvoiceDetail() ::: "+e.toString());
        }
        frmInvoiceDetail = new FrmInvoiceDetail(request, objInvoiceDetail);
    }
    
    public int getLanguage(){
        return iLanguage;
    }
    
    public void setLanguage(int iLanguage){
        this.iLanguage = iLanguage;
    }
    
    private String getSystemMassage(int msgCode){
        switch(msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID:
                return resultText[iLanguage][RSLT_VOUCHER_NUMBER_EXIST];
        }
        return resultText[iLanguage][RSLT_OK];
    }
    
    private int getControlMassageId(int msgCode){
        switch(msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_VOUCHER_NUMBER_EXIST;
        }
        return RSLT_OK;
    }
    
    public InvoiceDetail getInvoiceDetail(){
        return objInvoiceDetail;
    }
    
    public FrmInvoiceDetail getForm(){ 
        return frmInvoiceDetail; 
    }
    
    public String getMassage(){
        return msgString;
    }
    
    public int getErrorCode(){
        return iErrorCode;
    }
    
    public int action(int iCommand, long lInvoiceDetailId){
	return action(iCommand, lInvoiceDetailId, 0, 0, 0, 0, 0, 0,0,0);
    }
    
    public int action(int iCommand, long lInvoiceDetailId, long lTicketId, double netPrice, 
	    double commision, long supplierId, long accCogsId, long accAPId,double nrta, double surcharge){
	TicketList objTicketList = new TicketList();
	Costing objCosting = new Costing();
	long lastTicketId = 0;
        int iResult = RSLT_OK;
            switch(iCommand){
                case Command.ADD:
                break;
                case Command.SAVE:
                    frmInvoiceDetail.requestEntityObject(objInvoiceDetail);
                    objInvoiceDetail.setOID(lInvoiceDetailId);
                    
                    if(frmInvoiceDetail.errorSize() > 0){
                        msgString = resultText[iLanguage][RSLT_FORM_INCOMPLETE];
                        return RSLT_FORM_INCOMPLETE;
                    }
                    
                    if(objInvoiceDetail.getOID() == 0){
                        try{
                            long lOid = pstInvoiceDetail.insertExc(this.objInvoiceDetail);
			    if(lTicketId != 0){
				try{
				    objTicketList = PstTicketList.fetchExc(lTicketId);
				    objTicketList.setInvoiceDetailId(lOid);
				    objTicketList.setTicketPrice(netPrice);
				    long lUpdateTicket = PstTicketList.updateExc(objTicketList);
				}catch(Exception e){}
			    }
			    
			    InvoiceMain objInvMain = new InvoiceMain();
				try{
				    objInvMain = PstInvoiceMain.fetchExc(objInvoiceDetail.getInvoiceId());
				}catch(Exception e){}

				Date planDate = (Date)objInvMain.getIssuedDate();
				int iDate = 0;
				iDate = planDate.getDate() + objInvMain.getTermOfPayment();
				planDate.setDate(iDate);
				
			    if(objInvoiceDetail.getInvoiceId() != 0){
				double totalInvoice = SessInvoice.getTotalInvoice(objInvoiceDetail.getInvoiceId());
				int iTotDetail = PstInvoiceDetail.getCount(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+" = "+objInvoiceDetail.getInvoiceId());
				long paymentTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AR);
				if(totalInvoice > 0 && objInvMain.getTermOfPayment() > 0){
				    PaymentTerm objPaymentTerm = new PaymentTerm();
				    objPaymentTerm.setAmount(totalInvoice);
				    objPaymentTerm.setInvoiceId(objInvoiceDetail.getInvoiceId());
				    objPaymentTerm.setNote("Plan payment for invoice no. "+objInvMain.getInvoiceNumber());
				    objPaymentTerm.setPlanDate(planDate);
				    objPaymentTerm.setType(PstPaymentTerm.PAY_TERM_AR);
				    if(iTotDetail == 1){
					long insertPayTermAr = PstPaymentTerm.insertExc(objPaymentTerm);
				    }else{
					PaymentTerm objUpdatePaymentTerm = new PaymentTerm();
					if(paymentTermId != 0){
					    try{
						objUpdatePaymentTerm = PstPaymentTerm.fetchExc(paymentTermId);
						objUpdatePaymentTerm.setAmount(totalInvoice);
						long upPaymentTerm = PstPaymentTerm.updateExc(objUpdatePaymentTerm);
					    }catch(Exception e){}
					}
				    }
				}
				
				
			    }
			    
			    if(supplierId != 0 && accCogsId != 0 && accAPId != 0){
				    objCosting.setInvoiceId(objInvoiceDetail.getInvoiceId());
				    objCosting.setContactId(supplierId);
				    objCosting.setDescription(objInvoiceDetail.getDescription());
				    objCosting.setIdPerkiraanHpp(accCogsId);
				    objCosting.setIdPerkiraanHutang(accAPId);
				    objCosting.setNumber(1);
				    objCosting.setUnitPrice(nrta + surcharge - commision);
				    objCosting.setStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
				    objCosting.setInvoiceDetailId(objInvoiceDetail.getOID());
				    try{
					long insertCosting = PstCosting.insertExc(objCosting);
				    }catch(Exception e){}
				    
				double totalHpp = SessInvoice.getTotalHpp(objInvoiceDetail.getInvoiceId());
				int iTotCosting = PstCosting.getCount(PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+objInvoiceDetail.getInvoiceId());
				long apPaymentTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AP);
				if(totalHpp > 0){
				    
				    PaymentTerm objApPaymentTerm = new PaymentTerm();
				    objApPaymentTerm.setAmount(totalHpp);
				    objApPaymentTerm.setInvoiceId(objInvoiceDetail.getInvoiceId());
				    objApPaymentTerm.setNote("Plan payment costing for invoice no. "+objInvMain.getInvoiceNumber());
				    objApPaymentTerm.setPlanDate(planDate);
				    objApPaymentTerm.setType(PstPaymentTerm.PAY_TERM_AP);				    
				    
				    if(iTotCosting == 1){
					long insertPayTermAp = PstPaymentTerm.insertExc(objApPaymentTerm);	
				    }else{
					PaymentTerm objUpdApPayTerm = new PaymentTerm();
					if(apPaymentTermId != 0){
					    try{
						objUpdApPayTerm = PstPaymentTerm.fetchExc(apPaymentTermId);
						objUpdApPayTerm.setAmount(totalHpp);
						long lUpdateApPayTerm = PstPaymentTerm.updateExc(objUpdApPayTerm);
					    }catch(Exception e){}
					}
				    }
				}
			    }
                        }catch(Exception e){
                            //System.out.println("Exception on CtrlInvoiceDetail.insertExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }else{
			
			if(lTicketId != 0){
			    try{
				    lastTicketId = PstTicketList.getTicketListIdByInvDetailId(objInvoiceDetail.getOID());				    
				    if(lastTicketId != 0){
					TicketList objLastTicketList = new TicketList();
					objLastTicketList = PstTicketList.fetchExc(lastTicketId);
					objLastTicketList.setInvoiceDetailId(0);
					objLastTicketList.setTicketPrice(0);
					long lUpdateTicketList = PstTicketList.updateExc(objLastTicketList);
				    }
				    
				    objTicketList = PstTicketList.fetchExc(lTicketId);
				    objTicketList.setInvoiceDetailId(objInvoiceDetail.getOID());
				    objTicketList.setTicketPrice(netPrice);
				    //System.out.println("netPrice java :::::::::::::::::::::::::::::::::::::: "+netPrice);
				    long lUpdateTicketList = PstTicketList.updateExc(objTicketList);
				}catch(Exception e){}
			}
			
                        try{
                            long lOid = pstInvoiceDetail.updateExc(this.objInvoiceDetail);
                        }catch(Exception e){
                            System.out.println("Exception on CtrlInvoiceDetail.updateExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
			
			long costingId = PstCosting.getIdCosting(objInvoiceDetail.getOID());
			if(costingId != 0){
			    try{
				objCosting = PstCosting.fetchExc(costingId);
				long contactId = PstCosting.getSupplierId(objCosting, supplierId);
				String description = PstCosting.getDescription(objCosting, objInvoiceDetail.getDescription());
				long idHpp = PstCosting.getIdHpp(objCosting, accCogsId);
				long idPayable = PstCosting.getIdPayable(objCosting, accAPId);
				double netCoGS = nrta + surcharge - commision;
				double unitPrice = PstCosting.getUnitPrice(objCosting, netCoGS);
				
				objCosting.setContactId(contactId);
				objCosting.setDescription(description);
				objCosting.setIdPerkiraanHpp(idHpp);
				objCosting.setIdPerkiraanHutang(idPayable);
				objCosting.setUnitPrice(unitPrice);
				
				long lUpdateCosting = PstCosting.updateExc(objCosting);
			    }catch(Exception e){}
			}
			
			long arPayTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AR);
			double totalInvoice = SessInvoice.getTotalInvoice(objInvoiceDetail.getInvoiceId());
			if(arPayTermId != 0){
			    try{
				PaymentTerm objPayTerm = PstPaymentTerm.fetchExc(arPayTermId);
				objPayTerm.setAmount(totalInvoice);
				long lUpdateArPayTerm = PstPaymentTerm.updateExc(objPayTerm);
			    }catch(Exception e){}
			}
			
			long apPayTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AP);
			double totalCosting = SessInvoice.getTotalHpp(objInvoiceDetail.getInvoiceId());
			if(apPayTermId != 0){
			    try{
				PaymentTerm objPayTerm = PstPaymentTerm.fetchExc(apPayTermId);
				objPayTerm.setAmount(totalCosting);
				long lUpdateApPayTerm = PstPaymentTerm.updateExc(objPayTerm);
			    }catch(Exception e){}
			}
                    }
                break;
                case Command.EDIT:
                    if(lInvoiceDetailId != 0){
                        try{
                            objInvoiceDetail = pstInvoiceDetail.fetchExc(lInvoiceDetailId);
                        }catch(Exception e){
                            System.out.println("Exception on Edit CtrlInvoiceDetail.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.ASK:
                    if(lInvoiceDetailId != 0){
                        try{
                            objInvoiceDetail = pstInvoiceDetail.fetchExc(lInvoiceDetailId);
                        }catch(Exception e){
                            System.out.println("Exception on Ask CtrlInvoiceDetail.fetchExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
                    }
                break;
                case Command.DELETE:
                    if(lInvoiceDetailId != 0){
                        PstInvoiceMain pstInvMain = new PstInvoiceMain();
			if(lTicketId == 0){
			    lTicketId = PstTicketList.getTicketListIdByInvDetailId(lInvoiceDetailId);
			    if(lTicketId != 0){
				try{
				    objTicketList = PstTicketList.fetchExc(lTicketId);
				    objTicketList.setInvoiceDetailId(0);
				    objTicketList.setInvoiceDetailId(0);
				    long lUpdateTicketList = PstTicketList.updateExc(objTicketList);
				}catch(Exception e){}
			    }
			}
			
			long costingId = PstCosting.getIdCosting(lInvoiceDetailId);
			if(costingId != 0){
			    try{
			    long deleteCosting = PstCosting.deleteExc(costingId);
			    }catch(Exception e){}
			}
			
			objInvoiceDetail = new InvoiceDetail();
			try{
			    objInvoiceDetail = PstInvoiceDetail.fetchExc(lInvoiceDetailId);
			}catch(Exception e){}
			
			try{
                            long lOid = pstInvoiceDetail.deleteExc(lInvoiceDetailId);
                        }catch(Exception e){
                            System.out.println("Exception on CtrlInvoiceDetail.deleteExc() ::: "+e.toString());
                            msgString = resultText[iLanguage][RSLT_UNKNOWN_ERROR];
                            return RSLT_UNKNOWN_ERROR;
                        }
			
			double totalInvoice = SessInvoice.getTotalInvoice(objInvoiceDetail.getInvoiceId());
			int iTotDetail = PstInvoiceDetail.getCount(PstInvoiceDetail.fieldNames[PstInvoiceDetail.FLD_INVOICE_ID]+" = "+objInvoiceDetail.getInvoiceId());
			long arPayTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AR);
                        //System.out.println("CtrlInvoiceDetail.DELETE.iTotDetail :::::::: "+iTotDetail);
			if(iTotDetail == 0){
			    try{
				long deleteArPayTermId = PstPaymentTerm.deleteExc(arPayTermId);
			    }catch(Exception e){}
			}else{
			    PaymentTerm objUpArPayTerm = new PaymentTerm();
			    if(arPayTermId != 0){
				try{
				    objUpArPayTerm = PstPaymentTerm.fetchExc(arPayTermId);
				    objUpArPayTerm.setAmount(totalInvoice);
				    long lUpArPayTerm = PstPaymentTerm.updateExc(objUpArPayTerm);
				}catch(Exception e){}
			    }
			}
			
			long apPayTermId = SessInvoice.getIdPaymentTerm(objInvoiceDetail.getInvoiceId(), PstPaymentTerm.PAY_TERM_AP);
			double totalHpp = SessInvoice.getTotalHpp(objInvoiceDetail.getInvoiceId());
			int iTotCosting = PstCosting.getCount(PstCosting.fieldNames[PstCosting.FLD_INVOICE_ID]+" = "+objInvoiceDetail.getInvoiceId());
			//System.out.println("CtrlInvoiceDetail.DELETE.iTotCosting :::::::: "+iTotCosting);
			if(iTotCosting == 0){
			    try{
				long deleteApPayTermId = PstPaymentTerm.deleteExc(apPayTermId);
			    }catch(Exception e){}
			}else{
			    PaymentTerm objUpApPayTerm = new PaymentTerm();
			    if(apPayTermId != 0){
				try{
				    objUpApPayTerm = PstPaymentTerm.fetchExc(apPayTermId);
				    objUpApPayTerm.setAmount(totalHpp);
				    long lUpApPayTerm = PstPaymentTerm.updateExc(objUpApPayTerm);
				}catch(Exception e){}
			    }
			}
                    }
                break;
                default:
                break;    
            }
        return iResult;
    }
    
}
