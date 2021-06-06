/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class documentManager extends HttpServlet {

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
		int count = 0;
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
			JSONArray arrMatDispatch = jsonData.optJSONArray(PstMatDispatch.TBL_DISPATCH);
			for (int idxMatDispatch = 0; idxMatDispatch < arrMatDispatch.length(); idxMatDispatch++){
				count++;
				JSONObject obMatDispatch = arrMatDispatch.optJSONObject(idxMatDispatch);
				long oidMatDispatch = PstMatDispatch.syncExc(obMatDispatch);
				if (oidMatDispatch > 0){
					jSONObject.put(""+obMatDispatch.optLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID],0), true);
				} else {
					jSONObject.put(""+obMatDispatch.optLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID],0), false);
				}
				JSONArray arrDispatchItem = obMatDispatch.optJSONArray(PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM);
				if (arrDispatchItem != null){
					for (int idxDispatchItem = 0; idxDispatchItem < arrDispatchItem.length(); idxDispatchItem++){
						JSONObject objDispatchItem = arrDispatchItem.optJSONObject(idxDispatchItem);
						long  oidDispatchItem = PstMatDispatchItem.syncExc(objDispatchItem);
					}
				}
				
				JSONArray arrMatRec = obMatDispatch.optJSONArray(PstMatReceive.TBL_MAT_RECEIVE);
				if (arrMatRec != null){
					for (int idxMatRec = 0; idxMatRec < arrMatRec.length(); idxMatRec++){
						JSONObject objMatRec = arrMatRec.optJSONObject(idxMatRec);
						long oidMatRec = PstMatReceive.syncExc(objMatRec);
						JSONArray arrMatRecItem = objMatRec.optJSONArray(PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM);
						if (arrMatRecItem != null){
							for (int idxMatRecItem = 0; idxMatRecItem < arrMatRecItem.length(); idxMatRecItem++){
								JSONObject objMatRecItem = arrMatRecItem.optJSONObject(idxMatRecItem);
								long oidBillDetail = PstMatReceiveItem.syncExc(objMatRecItem);
							}
						}
					}
				}
			}
			
        } catch (Exception exc) {
			System.out.println("eror API : "+exc.toString()+ " "+ count);
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
