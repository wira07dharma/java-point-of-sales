/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.OtherCost;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstOtherCost;
import com.dimata.pos.entity.billing.PstRecipe;
import com.dimata.pos.entity.billing.Recipe;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashCreditPaymentInfo;
import com.dimata.pos.entity.payment.CashCreditPayments;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.CreditPaymentMain;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashCreditPayment;
import com.dimata.pos.entity.payment.PstCashCreditPaymentInfo;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.entity.payment.PstCreditPaymentMain;
import com.dimata.pos.form.balance.FrmCashCashier;
import com.dimata.pos.form.billing.FrmBillDetail;
import com.dimata.pos.form.billing.FrmBillMain;
import com.dimata.pos.form.masterCashier.FrmCashMaster;
import com.dimata.pos.form.payment.FrmCashCreditCard;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author IanRizky
 */
public class sendSales extends HttpServlet {

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;
    
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //LONG
    private long oid = 0;
    private long oidReturn = 0;
    private long userId = 0;
    private long empId = 0;
    private long datachange = 0;
    
    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String empName = "";
    
    
    
    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
	
	//Model
	private CashCashier cashCashier;
    private PstCashCashier pstCashCashier;
    private FrmCashCashier frmCashCashier;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        //LONG
	this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
	this.oidReturn=0;
	this.userId = FRMQueryString.requestLong(request, "userId");
	this.empId = FRMQueryString.requestLong(request, "empId");
	this.datachange = FRMQueryString.requestLong(request, "datachange");
	
	//STRING
	this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
	this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
	this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
	this.htmlReturn = "";
        this.empName = FRMQueryString.requestString(request, "empName");
	
	//INT
	this.iCommand = FRMQueryString.requestCommand(request);
	this.iErrCode = 0;
	
	
	
	//OBJECT
	this.jSONObject = new JSONObject();
	
	switch(this.iCommand){
	    case Command.SAVE :
			commandSave(request);
			break;
	}
	
	
	try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
	
	response.getWriter().print(this.jSONObject);
        
    }
    
    public void commandSave(HttpServletRequest request){
		if (dataFor.equals("cashCashier")){
			cashCashier(request);
		} else if (dataFor.equals("cashMaster")){
			cashMaster(request);
		} else if (dataFor.equals("cashBalance")){
			cashBalance(request);
		} else if (dataFor.equals("cashBillMain")){
			cashBillMain(request);
		} else if (dataFor.equals("cashBillDetail")){
			cashBillDetail(request);
		} else if (dataFor.equals("creditPaymentMain")){
			creditPaymentMain(request);
		} else if (dataFor.equals("cashOtherCost")){
			cashOtherCost(request);
		} else if (dataFor.equals("cashReturn")){
			cashReturnPayment(request);
		} else if (dataFor.equals("cashRecipe")){
			cashRecipe(request);
		} else if (dataFor.equals("cashPayments")){
			cashPayment(request);
		} else if (dataFor.equals("cashCreditCard")){
			cashCreditCard(request);
		} else if (dataFor.equals("creditPayment")){
			cashCreditCard(request);
		} else if (dataFor.equals("creditPaymentInfo")){
			cashCreditCard(request);
		}
    }
    
    public void cashCashier(HttpServletRequest request){
		try {
			cashCashier = new CashCashier();
			frmCashCashier = new FrmCashCashier(request, cashCashier);
			frmCashCashier.requestEntityObject(cashCashier);
			cashCashier.setOID(FRMQueryString.requestLong(request, FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CASH_CASHIER_ID]));
			
			boolean checkOid = PstCashCashier.checkOID(cashCashier.getOID());
			if (checkOid){
				PstCashCashier.updateExc(cashCashier);
			} else {
				PstCashCashier.insertExcByOid(cashCashier);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at CashCashier : "+ exc.toString());
		}
	}
    
	public void cashMaster(HttpServletRequest request){
		try {
			CashMaster cashMaster = new CashMaster();
			FrmCashMaster frmCashMaster = new FrmCashMaster(request, cashMaster);
			frmCashMaster.requestEntityObject(cashMaster);
			cashMaster.setOID(FRMQueryString.requestLong(request, FrmCashMaster.fieldNames[FrmCashMaster.FRM_FIELD_CASH_MASTER_ID]));
			
			boolean checkOid = PstCashMaster.checkOID(cashMaster.getOID());
			if (checkOid){
				PstCashMaster.updateExc(cashMaster);
			} else {
				PstCashMaster.insertExcByOid(cashMaster);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashMaster : "+ exc.toString());
		}
	}
	
	public void cashBalance(HttpServletRequest request){
		try {
			Balance balance = new Balance();
			balance.setOID(FRMQueryString.requestLong(request, "CASH_BALANCE_ID"));
			balance.setCashCashierId(FRMQueryString.requestLong(request, "CASH_CASHIER_ID"));
			balance.setCurrencyOid(FRMQueryString.requestLong(request, "CURRENCY_ID"));
			balance.setBalanceType(FRMQueryString.requestInt(request, "TYPE"));
			balance.setBalanceDate(FRMQueryString.requestDate(request, "BALANCE_DATE"));
			balance.setBalanceValue(FRMQueryString.requestDouble(request, "BALANCE_VALUE"));
			balance.setShouldValue(FRMQueryString.requestDouble(request, "SHOULD_VALUE"));
			
			boolean checkOid = PstBalance.checkOID(balance.getOID());
			if (checkOid){
				PstBalance.updateExc(balance);
			} else {
				PstBalance.insertExcByOid(balance);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashMaster : "+ exc.toString());
		}
	}
	
	public void cashBillMain(HttpServletRequest request){
		try {
			BillMain billMain = new BillMain();
			FrmBillMain frmBillMain = new FrmBillMain(request, billMain);
			frmBillMain.requestEntityObject(billMain);
			billMain.setOID(FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]));
			
			boolean checkOid = PstBillMain.checkOID(billMain.getOID());
			if (checkOid){
				PstBillMain.updateExc(billMain);
			} else {
				PstBillMain.insertExcByOid(billMain);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashMaster : "+ exc.toString());
		}
	}
	
	public void cashBillDetail(HttpServletRequest request){
		try {
			Billdetail billdetail = new Billdetail();
			FrmBillDetail frmBillDetail = new FrmBillDetail(request, billdetail);
			frmBillDetail.requestEntityObject(billdetail);
			billdetail.setOID(FRMQueryString.requestLong(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID]));
			
			boolean checkOid = PstBillDetail.checkOID(billdetail.getOID());
			if (checkOid){
				PstBillDetail.updateExc(billdetail);
			} else {
				PstBillDetail.insertExcByOid(billdetail);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashBillDetail : "+ exc.toString());
		}
	}
	
	public void creditPaymentMain(HttpServletRequest request){
		try {
			CreditPaymentMain creditPaymentMain = new CreditPaymentMain();
			creditPaymentMain.setOID(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]));
			creditPaymentMain.setBillMainId(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]));
			creditPaymentMain.setCashCashierId(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID]));
			creditPaymentMain.setLocationId(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_LOCATION_ID]));
			creditPaymentMain.setBillDate(FRMQueryString.requestDate(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_DATE]));
			creditPaymentMain.setAppUserId(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_APPUSER_ID]));
			creditPaymentMain.setShiftId(FRMQueryString.requestLong(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_SHIFT_ID]));
			creditPaymentMain.setBillStatus(FRMQueryString.requestInt(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_STATUS]));
			creditPaymentMain.setSalesCode(FRMQueryString.requestString(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_SALES_CODE]));
			creditPaymentMain.setInvoiceNumber(FRMQueryString.requestString(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_INVOICE_NUMBER]));
			creditPaymentMain.setInvoiceCounter(FRMQueryString.requestInt(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_INVOICE_COUNTER]));
			creditPaymentMain.setDocType(FRMQueryString.requestInt(request, PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_DOC_TYPE]));
			
			boolean checkOid = PstCreditPaymentMain.checkOID(creditPaymentMain.getOID());
			if (checkOid){
				PstCreditPaymentMain.updateExc(creditPaymentMain);
			} else {
				PstCreditPaymentMain.insertExcByOid(creditPaymentMain);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashBillDetail : "+ exc.toString());
		}
	}
	
	public void cashOtherCost(HttpServletRequest request){
		try {
			OtherCost otherCost = new OtherCost();
			otherCost.setOID(FRMQueryString.requestLong(request, PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID]));
			otherCost.setBillMainId(FRMQueryString.requestLong(request, PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]));
			otherCost.setName(FRMQueryString.requestString(request, PstOtherCost.fieldNames[PstOtherCost.FLD_NAME]));
			otherCost.setDescription(FRMQueryString.requestString(request, PstOtherCost.fieldNames[PstOtherCost.FLD_DESCRIPTION]));
			otherCost.setCurrencyId(FRMQueryString.requestLong(request, PstOtherCost.fieldNames[PstOtherCost.FLD_CURRENCY_ID]));
			otherCost.setRate(FRMQueryString.requestDouble(request, PstOtherCost.fieldNames[PstOtherCost.FLD_RATE]));
			otherCost.setAmount(FRMQueryString.requestDouble(request, PstOtherCost.fieldNames[PstOtherCost.FLD_AMOUNT]));
			
			
			boolean checkOid = PstOtherCost.checkOID(otherCost.getOID());
			if (checkOid){
				PstOtherCost.updateExc(otherCost);
			} else {
				PstOtherCost.insertExcByOid(otherCost);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashBillDetail : "+ exc.toString());
		}
	}
	
	public void cashReturnPayment(HttpServletRequest request){
		try {
			CashReturn cashReturn = new CashReturn();
			cashReturn.setOID(FRMQueryString.requestLong(request, PstCashReturn.fieldNames[PstCashReturn.FLD_RETURN_ID]));
			cashReturn.setCurrencyId(FRMQueryString.requestLong(request, PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]));
			cashReturn.setBillMainId(FRMQueryString.requestLong(request, PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]));
			cashReturn.setRate(FRMQueryString.requestDouble(request, PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]));
			cashReturn.setPaymentStatus(FRMQueryString.requestInt(request, PstCashReturn.fieldNames[PstCashReturn.FLD_PAYMENT_STATUS]));
			cashReturn.setAmount(FRMQueryString.requestDouble(request, PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
			
			
			boolean checkOid = PstCashReturn.checkOID(cashReturn.getOID());
			if (checkOid){
				PstCashReturn.updateExc(cashReturn);
			} else {
				PstCashReturn.insertExcByOid(cashReturn);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashBillDetail : "+ exc.toString());
		}
	}
	
	public void cashRecipe(HttpServletRequest request){
		try {
			Recipe recipe = new Recipe();
			recipe.setOID(FRMQueryString.requestLong(request, PstRecipe.fieldNames[PstRecipe.FLD_CASH_RECIPE_ID]));
			recipe.setCashBillMainId(FRMQueryString.requestLong(request, PstRecipe.fieldNames[PstRecipe.FLD_CASH_BILL_MAIN_ID]));
			recipe.setRecipeNumber(FRMQueryString.requestString(request, PstRecipe.fieldNames[PstRecipe.FLD_RECIPE_NUMBER]));
			recipe.setDoctorName(FRMQueryString.requestString(request, PstRecipe.fieldNames[PstRecipe.FLD_DOCTOR_NAME]));
			recipe.setPatientName(FRMQueryString.requestString(request, PstRecipe.fieldNames[PstRecipe.FLD_PATIENT_NAME]));
			recipe.setRecipeService(FRMQueryString.requestDouble(request, PstRecipe.fieldNames[PstRecipe.FLD_RECIPE_SERVICE]));
			
			
			boolean checkOid = PstRecipe.checkOID(recipe.getOID());
			if (checkOid){
				PstRecipe.updateExc(recipe);
			} else {
				PstRecipe.insertExcByOid(recipe);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashBillDetail : "+ exc.toString());
		}
	}
	
	public void cashPayment(HttpServletRequest request){
		try {
			CashPayments cashPayments = new CashPayments();
			cashPayments.setOID(FRMQueryString.requestLong(request, PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]));
			cashPayments.setPaymentStatus(FRMQueryString.requestInt(request, PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_STATUS]));
			cashPayments.setCurrencyId(FRMQueryString.requestLong(request, PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]));
			cashPayments.setBillMainId(FRMQueryString.requestLong(request, PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]));
			cashPayments.setPaymentType(FRMQueryString.requestInt(request, PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]));
			cashPayments.setRate(FRMQueryString.requestDouble(request, PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]));
			cashPayments.setPayDateTime(FRMQueryString.requestDate(request, PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
			cashPayments.setAmount(FRMQueryString.requestDouble(request, PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
			
			boolean checkOid = PstCashPayment.checkOID(cashPayments.getOID());
			if (checkOid){
				PstCashPayment.updateExc(cashPayments);
			} else {
				PstCashPayment.insertExcByOid(cashPayments);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashPayment : "+ exc.toString());
		}
	}
    
	public void cashCreditCard(HttpServletRequest request){
		try {
			CashCreditCard cashCreditCard = new CashCreditCard();
			FrmCashCreditCard frmCashCreditCard = new FrmCashCreditCard(request, cashCreditCard);
			frmCashCreditCard.requestEntityObject(cashCreditCard);
			cashCreditCard.setOID(FRMQueryString.requestLong(request, FrmCashCreditCard.fieldNames[FrmCashCreditCard.FRM_FIELD_CC_ID]));
			
			boolean checkOid = PstCashCreditCard.checkOID(cashCreditCard.getOID());
			if (checkOid){
				PstCashCreditCard.updateExc(cashCreditCard);
			} else {
				PstCashCreditCard.insertExcByOid(cashCreditCard);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashCreditCard : "+ exc.toString());
		}
	}
	
	public void cashCreditPayment(HttpServletRequest request){
		try {
			CashCreditPayments cashCreditPayments = new CashCreditPayments();
			cashCreditPayments.setOID(FRMQueryString.requestLong(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]));
			cashCreditPayments.setPaymentStatus(FRMQueryString.requestInt(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_STATUS]));
			cashCreditPayments.setCreditMainId(FRMQueryString.requestLong(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]));
			cashCreditPayments.setCurrencyId(FRMQueryString.requestLong(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]));
			cashCreditPayments.setPaymentType(FRMQueryString.requestInt(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]));
			cashCreditPayments.setRate(FRMQueryString.requestDouble(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_RATE]));
			cashCreditPayments.setPayDateTime(FRMQueryString.requestDate(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_DATETIME]));
			cashCreditPayments.setAmount(FRMQueryString.requestDouble(request, PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]));
			
			boolean checkOid = PstCashCreditPayment.checkOID(cashCreditPayments.getOID());
			if (checkOid){
				PstCashCreditPayment.updateExc(cashCreditPayments);
			} else {
				PstCashCreditPayment.insertExcByOid(cashCreditPayments);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashCreditPayments : "+ exc.toString());
		}
	}
	
	public void cashCreditPaymentInfo(HttpServletRequest request){
		try {
			CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
			cashCreditPaymentInfo.setOID(FRMQueryString.requestLong(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CREDIT_PAYMENT_INFO_ID]));
			cashCreditPaymentInfo.setCcName(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NAME]));
			cashCreditPaymentInfo.setCcNumber(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NUMBER]));
			cashCreditPaymentInfo.setExpiredDate(FRMQueryString.requestDate(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_EXPIRED_DATE]));
			cashCreditPaymentInfo.setHolderName(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_HOLDER_NAME]));
			cashCreditPaymentInfo.setDebitBankName(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_BANK_NAME]));
			cashCreditPaymentInfo.setDebitCardName(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_CARD_NAME]));
			cashCreditPaymentInfo.setChequeAccountName(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_ACCOUNT_NAME]));
			cashCreditPaymentInfo.setChequeDueDate(FRMQueryString.requestDate(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_DUE_DATE]));
			cashCreditPaymentInfo.setCurrencyId(FRMQueryString.requestLong(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CURRENCY_ID]));
			cashCreditPaymentInfo.setRate(FRMQueryString.requestDouble(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_RATE]));
			cashCreditPaymentInfo.setAmount(FRMQueryString.requestDouble(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_AMOUNT]));
			cashCreditPaymentInfo.setChequeBank(FRMQueryString.requestString(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_BANK]));
			cashCreditPaymentInfo.setPaymentId(FRMQueryString.requestLong(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]));
			cashCreditPaymentInfo.setTanggalPencairan(FRMQueryString.requestDate(request, PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_TANGGAL_CAIR]));
			
			boolean checkOid = PstCashCreditPaymentInfo.checkOID(cashCreditPaymentInfo.getOID());
			if (checkOid){
				PstCashCreditPaymentInfo.updateExc(cashCreditPaymentInfo);
			} else {
				PstCashCreditPaymentInfo.insertExcByOid(cashCreditPaymentInfo);
			}
			
		} catch (Exception exc){
			System.out.println("Exception at cashCreditPayments : "+ exc.toString());
		}
	}
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
