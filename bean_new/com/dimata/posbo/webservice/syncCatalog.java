/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
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
public class syncCatalog extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	
	//OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String address = "";
    private String htmlReturn = "";
    private String searchTerm = "";
    private int amount = 0;
    private String colName = "";
    private String dir = "";
    private int start = 0;
    private int colOrder = 0;
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int language = 0;
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			long locationId = FRMQueryString.requestLong(request, "LOCATION_ID");
			this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");

			if (dataFor.equals("getData")){
				String whereClause = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+"="+locationId
							+ " AND "+ PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+"= 'sync_data'";
				int countData  = PstDataCustom.getCount(whereClause);
				Vector listData = PstDataCustom.list(0, 0, whereClause, "data_count");

				jSONArray = new JSONArray();
				if (listData.size()>0){
					for (int i=0; i < listData.size(); i++){
						DataCustom dataCustom = (DataCustom) listData.get(i);
						JSONObject objData = new JSONObject();
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_CUSTOM_ID], ""+dataCustom.getOID());
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID], ""+dataCustom.getOwnerId());
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME], ""+dataCustom.getDataName());
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_LINK], ""+dataCustom.getLink());
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE], ""+dataCustom.getDataValue());
						objData.put(PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_COUNT], ""+dataCustom.getDataCount());
						jSONArray.put(objData);
					}
				}
				jSONObject.put("COUNT", ""+countData);
				jSONObject.put("DATA", jSONArray);
			} else {
				String[] oids = FRMQueryString.requestStringValues(request, PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_CUSTOM_ID]);
				if (oids != null){
					for (String oid : oids){
						try {
							PstDataCustom.deleteExc(Long.valueOf(oid));
						} catch (Exception exc){
							
						}
					}
				}
			}
			
		} catch (Exception exc){}
		
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
