/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.contact.PstContactList;
import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashReturn;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author IanRizky
 */
public class salesManager extends HttpServlet {

	private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
	
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		jSONObject = new JSONObject();
        try {
			InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            String jsonString = out.toString();
			JSONObject jsonData = new JSONObject(jsonString);
			JSONArray arrCashCashier = jsonData.optJSONArray(PstCashCashier.TBL_CASH_CASHIER);
			for (int idxCashCashier = 0; idxCashCashier < arrCashCashier.length(); idxCashCashier++){
				JSONObject objCashCashier = arrCashCashier.optJSONObject(idxCashCashier);
				long oidCashCashier = PstCashCashier.syncExc(objCashCashier);
				JSONArray arrCashBalance = objCashCashier.optJSONArray(PstBalance.TBL_BALANCE);
				if (arrCashBalance != null){
					for (int idxCashBalance = 0; idxCashBalance < arrCashBalance.length(); idxCashBalance++){
						JSONObject objCashBalance = arrCashBalance.optJSONObject(idxCashBalance);
						long  oidCashBalance = PstBalance.syncExc(objCashBalance);
					}
				}
				JSONArray arrBillMain = objCashCashier.optJSONArray(PstBillMain.TBL_CASH_BILL_MAIN);
				if (arrBillMain != null){
					for (int idxBillMain = 0; idxBillMain < arrBillMain.length(); idxBillMain++){
						JSONObject objBillMain = arrBillMain.optJSONObject(idxBillMain);
						long oidBillMain = PstBillMain.syncExc(objBillMain);
						if (oidBillMain > 0){
							jSONObject.put(""+objBillMain.optLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID],0), true);
						} else {
							jSONObject.put(""+objBillMain.optLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID],0), false);
						}
						JSONArray arrBillDetail = objBillMain.optJSONArray(PstBillDetail.TBL_CASH_BILL_DETAIL);
						if (arrBillDetail != null){
							for (int idxBillDetail = 0; idxBillDetail < arrBillDetail.length(); idxBillDetail++){
								JSONObject objBillDetail = arrBillDetail.optJSONObject(idxBillDetail);
								long oidBillDetail = PstBillDetail.syncExc(objBillDetail);
							}
						}
						JSONArray arrCashPayment = objBillMain.optJSONArray(PstCashPayment.TBL_PAYMENT);
						if (arrCashPayment != null){
							for (int idxCashPayment = 0; idxCashPayment < arrCashPayment.length(); idxCashPayment++){
								JSONObject objCashPayment = arrCashPayment.optJSONObject(idxCashPayment);
								long oidCashPayment = PstCashPayment.syncExc(objCashPayment);
								JSONArray arrCashCreditCard = objCashPayment.optJSONArray(PstCashCreditCard.TBL_CREDIT_CARD);
								if (arrCashCreditCard != null){
									for (int idxCashCreditCard = 0; idxCashCreditCard < arrCashCreditCard.length(); idxCashCreditCard++){
										JSONObject objCashCreditCard = arrCashCreditCard.optJSONObject(idxCashCreditCard);
										long oidCashCreditCard = PstCashCreditCard.syncExc(objCashCreditCard);
									}
								}
							}
						}
						JSONArray arrCashReturnPayment = objBillMain.optJSONArray(PstCashReturn.TBL_RETURN);
						if (arrCashReturnPayment != null){
							for (int idxCashReturn = 0; idxCashReturn < arrCashReturnPayment.length(); idxCashReturn++){
								JSONObject objCashReturn = arrCashReturnPayment.optJSONObject(idxCashReturn);
								long oidCashReturn = PstCashReturn.syncExc(objCashReturn);
							}
						}
					}
				}
			}
			
        } catch (Exception exc) {

        }
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONObject);
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
