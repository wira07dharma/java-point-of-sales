/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice.masterdata;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.qdep.form.FRMQueryString;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
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
public class SalesService extends HttpServlet {

	private JSONObject jSONObject = new JSONObject();
	
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

//			String keyword = FRMQueryString.requestString(request, "keyword");
//			int limitStart = FRMQueryString.requestInt(request, "limitStart");
//			int recordToGet = FRMQueryString.requestInt(request, "recordToGet");
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String jsonString = buffer.toString();

            JSONObject jsonData;

            try {
                jsonData = new JSONObject(jsonString);
                String keyword = jsonData.getString("keyword");
                long oid = jsonData.optLong("oid", 0);
                int limitStart = jsonData.getInt("limitStart");
                int recordToGet = jsonData.getInt("recordToGet");

                if (oid > 0) {
                    try {
                        Sales sales = PstSales.fetchExc(oid);

                        jSONObject = PstSales.fetchJSON(oid);
                        JSONObject objLoc = PstLocation.fetchJSON(sales.getLocationId());
                        jSONObject.put(PstLocation.TBL_P2_LOCATION, objLoc);

                        JSONObject objEmployee = PstEmployee.fetchJSON(sales.getEmployeeId());
                        jSONObject.put(PstEmployee.TBL_HR_EMPLOYEE, objEmployee);
                    } catch (Exception exc) {

                    }

                } else {
                    String whereClause = PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '%" + keyword + "%'"
                            + " OR " + PstSales.fieldNames[PstSales.FLD_NAME] + " LIKE '%" + keyword + "%'";
                    Vector listData = PstSales.list(limitStart, recordToGet, whereClause, "SALES_CODE");
                    Vector listDataAll = PstSales.list(0, 0, whereClause, "SALES_CODE");
                    JSONArray jSONArray = new JSONArray();
                    if (listData.size() > 0) {
                        for (int i = 0; i < listData.size(); i++) {
                            Sales sales = (Sales) listData.get(i);
                            JSONObject objData = new JSONObject();
                            objData.put(PstSales.fieldNames[PstSales.FLD_ASSIGN_LOCATION_WAREHOUSE], "" + sales.getLocationId());
                            objData.put(PstSales.fieldNames[PstSales.FLD_CODE], "" + sales.getCode());
                            objData.put(PstSales.fieldNames[PstSales.FLD_COMMISION], "" + sales.getCommision());
                            objData.put(PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID], "" + sales.getEmployeeId());
                            objData.put(PstSales.fieldNames[PstSales.FLD_LOGIN_ID], "" + sales.getLoginId());
                            objData.put(PstSales.fieldNames[PstSales.FLD_NAME], "" + sales.getName());
                            objData.put(PstSales.fieldNames[PstSales.FLD_PASSWORD], "" + sales.getPassword());
                            objData.put(PstSales.fieldNames[PstSales.FLD_REMARK], "" + sales.getRemark());
                            objData.put(PstSales.fieldNames[PstSales.FLD_SALES_ID], "" + sales.getOID());
                            objData.put(PstSales.fieldNames[PstSales.FLD_STANDARD_CURRENCY_ID], "" + sales.getDefaultCurrencyId());
                            objData.put(PstSales.fieldNames[PstSales.FLD_STATUS], "" + sales.getStatus());

                            JSONObject objLoc = PstLocation.fetchJSON(sales.getLocationId());
                            objData.put(PstLocation.TBL_P2_LOCATION, objLoc);

                            JSONObject objEmployee = PstEmployee.fetchJSON(sales.getEmployeeId());
                            objData.put(PstEmployee.TBL_HR_EMPLOYEE, objEmployee);

                            //						objData.put(PstLocation.fieldNames[PstLocation.FLD_NAME], ""+loc.getName());
                            //						objData.put(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], ""+emp.getFullName());
                            jSONArray.put(objData);
                        }

                        jSONObject.put("COUNT", listDataAll.size());
                    }
                    jSONObject.put("DATA", jSONArray);
                }

                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(this.jSONObject);
            } catch (JSONException err) {

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
		//processRequest(request, response);
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
