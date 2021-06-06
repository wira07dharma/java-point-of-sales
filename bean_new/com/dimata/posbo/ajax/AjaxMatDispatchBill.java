/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatDispatchBill;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchBill;
import com.dimata.posbo.form.warehouse.CtrlMatDispatch;
import com.dimata.posbo.form.warehouse.CtrlMatDispatchBill;
import com.dimata.posbo.form.warehouse.FrmMatDispatch;
import com.dimata.posbo.form.warehouse.FrmMatDispatchBill;
import com.dimata.posbo.session.warehouse.SessMatDispatch;
import com.dimata.qdep.entity.I_IJGeneral;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author arise
 */
public class AjaxMatDispatchBill extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		int iCommand = FRMQueryString.requestCommand(request);
		String responses = "";
		long oid = FRMQueryString.requestLong(request, "oid");
		String type = FRMQueryString.requestString(request, "type");
		System.out.println(type);
		if (iCommand == Command.SAVE) {
			if(type.equals("insertInvoice")){
				JSONObject messages = new JSONObject();
				long oidDispatch = FRMQueryString.requestLong(request, "hidden_dispatch_id");
				int status = FRMQueryString.requestInt(request, FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_STATUS]);
				String[] oids = FRMQueryString.requestStringValues(request, FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_CASH_BILL_MAIN_ID]);
				for (int i = 0; i < oids.length; i++) {
					try {
						MatDispatchBill mdb = new MatDispatchBill();
						mdb.setStatus(status);
						mdb.setMatItemOid(oidDispatch);
						mdb.setCashBillOid(Long.parseLong(oids[i]));
						System.out.println(status+" "+oidDispatch+" "+Long.parseLong(oids[i]));
						long oidRes = PstMatDispatchBill.insertExc(mdb);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				responses = "success";
				try {
					messages.put("oid", responses);
				} catch (JSONException e) {
					Logger.getLogger(AjaxMatDispatchBill.class.getName()).log(Level.SEVERE, null, e);
				}
				out.print(messages);
			} else if(type.equals("newdocument")){
				JSONObject messages = new JSONObject();
				MatDispatch md = new MatDispatch();
//				CtrlMatDispatch cmd = new CtrlMatDispatch(request);
//				cmd.action(iCommand, oid);
				long locationOid = FRMQueryString.requestLong(request, FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID]);
				long dispatchOid = FRMQueryString.requestLong(request, FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO]);
				Date dateTime = ControlDate.getDateTime(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], request);
				
				md.setLocationId(locationOid);
				md.setDispatchTo(dispatchOid);
				md.setDispatchDate(dateTime);
				md.setJenisDokumen("PPBTBB");
				
				SessMatDispatch sessDispatch = new SessMatDispatch();
				int maxCounter = sessDispatch.getMaxDispatchCounter(md.getDispatchDate(), md);
				maxCounter = maxCounter + 1;
				md.setDispatchCodeCounter(maxCounter);
				md.setDispatchCode(sessDispatch.generateDispatchCode(md));
				md.setLast_update(new Date());
				
				long oidRes = 0;
				try {
					oidRes = PstMatDispatch.insertExc(md);
				} catch (Exception e) {
					e.printStackTrace();
				}

				PstDocLogger.insertDataBo_toDocLogger(md.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, md.getLast_update(), md.getRemark());
				responses = Long.toString(oidRes);
				try {
					messages.put("oid", responses);
				} catch (JSONException e) {
					Logger.getLogger(AjaxMatDispatchBill.class.getName()).log(Level.SEVERE, null, e);
				}
				out.print(messages);
			}
		} else if(iCommand == Command.LIST){
			JSONObject lists = new JSONObject();
			lists = PstMatDispatchBill.listInvoices(0, 0, "", "");
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			out.print(lists);
		} else if (iCommand == Command.DELETE){
			JSONObject messages = new JSONObject();
			CtrlMatDispatchBill cmdb = new CtrlMatDispatchBill(request);
			cmdb.action(iCommand, oid);
			responses = cmdb.getMessage();
			try {
				messages.put("oid", responses);
			} catch (JSONException e) {
				Logger.getLogger(AjaxMatDispatchBill.class.getName()).log(Level.SEVERE, null, e);
			}
			out.print(messages);
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
