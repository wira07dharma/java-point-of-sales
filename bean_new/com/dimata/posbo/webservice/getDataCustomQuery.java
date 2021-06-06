/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.form.FRMQueryString;
import java.io.IOException;
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
public class getDataCustomQuery extends HttpServlet {

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
		
		long locationId = FRMQueryString.requestLong(request, "LOCATION_ID");
		String dataFor = FRMQueryString.requestString(request, "DATAFOR");
		int fetchData = FRMQueryString.requestInt(request, "FETCH_DATA");
		
		try {
			if (dataFor.equals("listQuery")){
				String where = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+"="+locationId
						+ " AND "+ PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+"= 'sync_data'";
				Vector listDataCustom = PstDataCustom.list(0, fetchData, where, "data_count");
				if (listDataCustom.size()>0){
					jSONObject.put("size", listDataCustom.size());
					jSONArray = new JSONArray();
					for (int i = 0; i < listDataCustom.size();i++){
						DataCustom dataCustom = (DataCustom) listDataCustom.get(i);
						JSONObject query = new JSONObject();
						query.put("oid", dataCustom.getOID());
						query.put("count", dataCustom.getDataCount());
						query.put("query", dataCustom.getDataValue());
						jSONArray.put(query);
					}
					jSONObject.put("data", jSONArray);
				}
			} else if (dataFor.equals("delete")){
				String oid = FRMQueryString.requestString(request, "OID");
				if (oid.length()>0){
					DBHandler.execUpdate("DELETE FROM data_custom WHERE DATA_CUSTOM_ID IN ("+oid+") ");
				}
				
			} else if (dataFor.equals("listAll")){
				String where = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+"="+locationId
						+ " AND "+ PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+"= 'sync_data'";
				int size = PstDataCustom.getCount(where);
				jSONObject.put("sizeAll", size);
			}
			
		} catch (Exception exc){
		
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
