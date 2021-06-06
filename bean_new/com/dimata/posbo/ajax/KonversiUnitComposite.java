/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.ajax;

import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.qdep.form.FRMQueryString;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author dimata005
 */
public class KonversiUnitComposite extends HttpServlet{
     /* Generated by Together */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try{
            //String buffer="false";

            /**
             * create fungsi check qty stock
             */
            double qtyInput = FRMQueryString.requestLong(request,"FRM_FIELD_QTY_INPUT");
            long oidUnitKonversi = FRMQueryString.requestLong(request,"FRM_FIELD_UNIT_ID_KONVERSI");
            long oidUnit = FRMQueryString.requestLong(request,"FRM_FIELD_UNIT_ID");

            //create fungsi cek Nilai Konversi

            double valueKonfersi =  PstUnit.getQtyPerBaseUnit(oidUnit,oidUnitKonversi);
            double resultKonversi = 0.0;
            if(valueKonfersi!=0){
                resultKonversi=qtyInput/valueKonfersi;
            }else{
                resultKonversi=qtyInput;
            }
            
            response.getWriter().println(resultKonversi);

         }catch(Exception ex){
            ex.printStackTrace();
         }
    }
}
